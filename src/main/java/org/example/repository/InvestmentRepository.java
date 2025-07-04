package org.example.repository;

import org.example.exception.AccountWithInvestmentException;
import org.example.exception.InvestmentNotFoundException;
import org.example.exception.WalletNotFoundException;
import org.example.model.AccountWallet;
import org.example.model.Investment;
import org.example.model.InvestmentWallet;

import java.util.ArrayList;
import java.util.List;

import static org.example.repository.CommonsRepository.checkFundsForTransaction;

public class InvestmentRepository {

    private long nextId;
    private final List<Investment> investments = new ArrayList<>();
    private final List<InvestmentWallet> wallets = new ArrayList<>();

    public Investment create(final long tax, final long initialFunds) {
        this.nextId ++;

        var investment = new Investment(this.nextId, tax, initialFunds);
        investments.add(investment);
        return investment;
    }

    public InvestmentWallet initInvestment(final AccountWallet account, final long id){

        var accountsInUse = wallets.stream().map(InvestmentWallet::getAccount).toList();
        if (accountsInUse.contains(account)){
            throw new AccountWithInvestmentException("A conta '" + account + "' já está em uso.");
        }
        var investment = findById(id);

        checkFundsForTransaction(account, investment.initialFunds());

        var wallet = new InvestmentWallet(investment, account, investment.initialFunds());

        wallets.add(wallet);


        return wallet;

    }

    public InvestmentWallet deposit(final String pix, final long funds) {
        var wallet = findWalletByAccount(pix);
        wallet.addMoney(wallet.getAccount().reduceMoney(funds), wallet.getService(), "depósito de investimento");

        return wallet;
    }

    public InvestmentWallet withdraw(final String pix, final long funds){
        var wallet = findWalletByAccount(pix);
        checkFundsForTransaction(wallet,funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds), wallet.getService(), "retirada de investimento");

        if (wallet.getFunds() == 0) {
            wallets.remove(wallet);
        }

        return wallet;
    }

    public void updateAmount() {
        wallets.forEach(w -> w.updateAmount(w.getInvestment().tax()));
    }

    public Investment findById(final long id){
        return investments.stream()
                .filter(i -> i.id() == id)
                .findFirst()
                .orElseThrow(
                        () -> new InvestmentNotFoundException("O investimento não foi encontrado")
                );
    }

    public InvestmentWallet findWalletByAccount(final String pix){
        return wallets.stream()
                .filter(w -> w.getAccount().getPix().contains(pix))
                .findFirst()
                .orElseThrow(
                () -> new WalletNotFoundException("A carteira não foi encontrada")
        );
    }

    public List<InvestmentWallet> listWallets(){
        return this.wallets;

    }
    public List<Investment> list(){
        return this.investments;
    }
}
