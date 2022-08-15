package com.votelimes.lps.model.enums;

public enum Gender implements DisplayableInRussian{
    male, female, other;
    public String getInRussian(){
        switch (this){
            case male: return "Мужской";
            case female: return "Женский";
            default: return "Другое";
        }
    }
    public static String getInOrder(int order){
        switch (order){
            case 0: return "Мужской";
            case 1: return "Женский";
            default: return "Другое";
        }
    }

    public static Gender getInOrderGender(int order){
        switch (order){
            case 0: return Gender.male;
            case 1: return Gender.female;
            default: return Gender.other;
        }
    }
}
