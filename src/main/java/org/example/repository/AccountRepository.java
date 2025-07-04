package org.example.repository;

import org.example.exception.AccountNotFoundException;
import org.example.exception.AccountWithInvestmentException;
import org.example.exception.PixInUseException;
import org.example.model.AccountWallet;
import org.example.model.Investment;

import java.util.List;

import static org.example.repository.CommonsRepository.checkFundsForTransaction;

public class AccountRepository {

    private List<AccountWallet> accounts;

    public AccountWallet create(final List<String> pix, final long initialFunds){
        var pixInUse = accounts.stream().flatMap(a -> a.getPix().stream()).toList();
        for (var p : pix) {
            if (pixInUse.contains(p)){
                throw new AccountWithInvestmentException("O PIX '" + p + "' já está em uso.");
            }
        }
        var newAccount = new AccountWallet(initialFunds, pix);
        accounts.add(newAccount);

        return newAccount;
    }

    public void deposit(final String pix, final long fundsAmount){
        var target = findByPix(pix);
        target.addMoney(fundsAmount, "depósito na conta");
    }

    public long withdraw(final String pix, final long amount){
        var source = findByPix(pix);

        checkFundsForTransaction(source, amount);

        source.reduceMoney(amount);

        return amount;
    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount){
        var source = findByPix(sourcePix);
        checkFundsForTransaction(source, amount);
        var target = findByPix(targetPix);
        var message = "pix enviado de '" + sourcePix + "' para '" + targetPix + "'";

        target.addMoney(source.reduceMoney(amount), source.getService(), message);
    }

    public List<AccountWallet> list(){
        return this.accounts;
    }

    public AccountWallet findByPix(final String pix) {
        return accounts.stream()
                .filter(a -> a.getPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada com o PIX: " + pix));
    }

}
