package org.example.model;

import lombok.Getter;

import java.util.List;

import static org.example.model.BankService.ACCOUNT;

@Getter
public class AccountWallet extends Wallet{

    private final List<String> pix;

    public AccountWallet(final List<String> pix) {
        super(ACCOUNT);
        this.pix = pix;
    }

    public AccountWallet(final long amount, final List<String> pix){
        super(ACCOUNT);
        this.pix = pix;
        addMoney(amount, "valor de crianção da conta");
    }

    public void addMoney(final long amount, final String description) {
        var money = generateMoney(amount, description);
        this.money.addAll(money);
    }
}
