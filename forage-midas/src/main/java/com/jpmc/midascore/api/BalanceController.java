package com.jpmc.midascore.api;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    private final UserRepository userRepo;

    public BalanceController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // GET /balance?userId=123
    @GetMapping("/balance")
    public Balance getBalance(@RequestParam("userId") long userId) {
        float value = userRepo.findById(Long.valueOf(userId))
                .map(UserRecord::getBalance)
                .orElse(0.0f);
        return new Balance(value); // Do NOT change Balance.toString()
    }
}
