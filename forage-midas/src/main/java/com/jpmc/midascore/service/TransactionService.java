package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class TransactionService {

    private final UserRepository userRepo;
    private final TransactionRepository txRepo;
    private final RestTemplate restTemplate;
    private final String incentiveUrl;

    public TransactionService(UserRepository userRepo,
                              TransactionRepository txRepo,
                              RestTemplate restTemplate,
                              @Value("${incentive.url:http://localhost:8080/incentive}") String incentiveUrl) {
        this.userRepo = userRepo;
        this.txRepo = txRepo;
        this.restTemplate = restTemplate;
        this.incentiveUrl = incentiveUrl;
    }

    @Transactional
    public boolean process(Transaction tx) {
        if (tx == null) return false;

        float amount = tx.getAmount();
        if (amount <= 0f) return false;

        var senderOpt = userRepo.findById(Long.valueOf(tx.getSenderId()));
        var recipientOpt = userRepo.findById(Long.valueOf(tx.getRecipientId()));
        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) return false;

        var sender = senderOpt.get();
        var recipient = recipientOpt.get();

        if (sender.getBalance() < amount) return false;

        // 1) Call incentive API (best-effort)
        float incentive = 0f;
        try {
            var resp = restTemplate.postForObject(incentiveUrl, tx, Incentive.class);
            if (resp != null && resp.getAmount() > 0f) incentive = resp.getAmount();
        } catch (Exception ignore) {
        }

        // 2) Apply balances
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount + incentive);

        // 3) Persist users
        userRepo.save(sender);
        userRepo.save(recipient);

        // 4) ⬇️ Persist the transaction record *here*
        txRepo.save(new TransactionRecord(sender, recipient, amount, incentive));

        return true;
    }
}