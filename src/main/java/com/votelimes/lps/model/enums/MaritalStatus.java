package com.votelimes.lps.model.enums;

public enum MaritalStatus implements DisplayableInRussian{
    single, married;
    public String getInRussian(){
        switch (this){
            case single: return "Холост / Не замужем";
            case married: return "Женат / Жената";
            default: return "Другое";
        }
    }
    public static MaritalStatus getInOrderMaritalStatus(int order){
        switch (order){
            case 0: return MaritalStatus.single;
            case 1: return MaritalStatus.married;
            default: return null;
        }
    }
}
