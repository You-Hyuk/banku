package transaction;

import database.AccountDao;
import database.DatabaseManager;
import database.UserDao;

import java.io.IOException;
import java.util.Scanner;

public class DepositService {
    private final Scanner scanner;
    private final UserDao userDao;
    private final AccountDao accountDao;

    public DepositService(UserDao userDao, AccountDao accountDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.scanner = new Scanner(System.in);
        initializeServices();
    }

    private void initializeServices() {
        DatabaseManager dbManager = new DatabaseManager();
        UserDao userDao = new UserDao(dbManager);
    }


    public void showDeposit(String loggedInUserId){
        try{
            // 입금 서비스 시작
            System.out.println("\n\n[입금 서비스]");
            System.out.println("============================================");
            System.out.println("('q'를 입력할 시 이전 화면으로 돌아갑니다.)");
            System.out.println("============================================");
            System.out.println("입금할 금액을 입력하세요");

            String money = "";

            while (true) {
                // 입금 금액 입력 받기
                System.out.print("입금할 금액: ₩ ");
                money = scanner.nextLine();

                if (money.equals("q")) {
                    return;
                }

                if (!money.matches("\\d+")) {
                    System.out.println();
                    System.out.println("올바른 양식이 아닙니다.");
                } else if (Long.parseLong(money)>=0 && Long.parseLong(money) < 1000){
                    System.out.println("최소 금액 1000원 이상 입력해주세요.");
                } else {
                    break;
                }
            }

            // 입금 금액 입력 받기
            long amount = Long.parseLong(money);

            // id 해당 계좌
            String account = userDao.findUserToAccount(loggedInUserId);
            accountDao.depositSavings(account, amount);

            System.out.println();
            System.out.println("입금이 완료되었습니다!");
            System.out.println("현재 잔액: ₩ " + accountDao.showSavings(account));
        } catch (IOException e){
            System.out.println("입금 중 오류가 발생했습니다: " + e.getMessage());
        }

    }
}