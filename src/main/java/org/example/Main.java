package org.example;

import org.example.exception.AccountNotFoundException;
import org.example.exception.NoFundsEnoughException;
import org.example.repository.AccountRepository;
import org.example.repository.InvestmentRepository;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {



        System.out.println("Olá, seja bem vindo ao Lucas Bank");
        
        while (true){
            System.out.println("Selecione a operação desejada: ");
            System.out.println("1 - Criar conta");
            System.out.println("2 - Criar um investimento");
            System.out.println("3 - Fazer um investimento");
            System.out.println("4 - Depositar na conta");
            System.out.println("5 - Sacar da conta");
            System.out.println("6 - Transferência entre contas");
            System.out.println("7 - Investir");
            System.out.println("8 - Sacar investimento");
            System.out.println("9 - Listar contas ");
            System.out.println("10 - Listar investimentos");
            System.out.println("11 - Listar carteiras de investimento");
            System.out.println("12 - Atualizar investimentos");
            System.out.println("13 - Histórico de contas");
            System.out.println("14 - Sair");

            var option = scanner.nextInt();
            switch (option){
                case 1: createAccount();
                case 2: createInvestment();
                case 3:
                case 4: deposit();
                case 5: withdraw();
                case 6:
                case 7:
                case 8:
                case 9: accountRepository.list().forEach(System.out::println);
                case 10: investmentRepository.list().forEach(System.out::println);
                case 11: investmentRepository.listWallets().forEach(System.out::println);
                case 12: {
                    investmentRepository.updateAmount();
                    System.out.println("Investimentos atualizados com sucesso.");
                }
                case 13:
                case 14: System.exit(0);
                default: System.out.println("Opção inválida, tente novamente.");

            }
        }
    }

    private static void createAccount(){
        System.out.println("Informe as chaves pix (separadas por ';'): ");
        var pix = Arrays.stream(scanner.next().split(";")).toList();
        System.out.println("Informe o valor inicial de depósito: ");

        var amount = scanner.nextLong();

        var wallet = accountRepository.create(pix, amount);

        System.out.println("Conta criada: " + wallet);
    }

    private static void createInvestment(){
        System.out.println("Informe a taxa do investimento: ");
        var tax = scanner.nextInt();
        System.out.println("Informe o valor inicial de depósito: ");

        var initialFunds = scanner.nextLong();

        var investment = investmentRepository.create(tax, initialFunds);

        System.out.println("Investimento criado: " + investment);
    }

    private static void deposit(){
        System.out.println("Informe o PIX da conta: ");
        var pix = scanner.next();
        System.out.println("Informe o valor a ser depositado: ");
        var amount = scanner.nextLong();

        try {
            accountRepository.deposit(pix, amount);
            System.out.println("Depósito realizado com sucesso.");

        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }

    }

    private static void withdraw(){
        System.out.println("Informe o PIX da conta: ");
        var pix = scanner.next();
        System.out.println("Informe o valor a ser sacado: ");
        var amount = scanner.nextLong();
        try {
            var withdrawnAmount = accountRepository.withdraw(pix, amount);
            System.out.println("Saque realizado com sucesso. Valor sacado: " + withdrawnAmount);

        } catch (NoFundsEnoughException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }

    }
}