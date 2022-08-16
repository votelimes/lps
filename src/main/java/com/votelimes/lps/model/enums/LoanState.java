package com.votelimes.lps.model.enums;

// Статусы заявки, подаваемой пользователем. Из-за работы CreditManager, после создания заявки пользователем, она с 50%
// вероятностью становится approved или rejected. Менеджер может вручную менять эти статусы в manager/application, если
// заявка не подписана пользователем.
public enum LoanState implements DisplayableInRussian{
    created, rejected, approved, signed, completed;
    public String getInRussian(){
        switch (this){
            case created: return "Создано, ждет решения";
            case rejected: return "Отклонено";
            case approved: return "Одобрено, ждет подписи пользователем";
            case signed: return "Подписано пользователем, ждет завершения";
            case completed: return "Завершено";
            default: return "Другое";
        }
    }
    public static LoanState getInOrder(int order){
        switch (order){
            case 0: return created;
            case 1: return rejected;
            case 2: return approved;
            case 3: return signed;
            case 4: return completed;
            default: return null;
        }
    }

    public int toInt(){
        switch (this){
            case created: return 0;
            case rejected: return 1;
            case approved: return 2;
            case signed: return 3;
            case completed: return 4;
        }
        return -1;
    }
}
