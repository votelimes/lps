package com.votelimes.lps.repo;

import com.votelimes.lps.model.SupplementedUser;
import com.votelimes.lps.model.enums.LoanState;

import java.util.List;

// Интерфейс для билдера составного запроса с несколькими параметрами. Реализован в SupplementedUserDaoImpl.Builder;
public interface SupplementedUserQueryBuilder {
    public SupplementedUserQueryBuilder setFullName(String fullName);
    public SupplementedUserQueryBuilder setLoanState(LoanState loanState);
    public SupplementedUserQueryBuilder setContactNumber(String contactNumber);
    public SupplementedUserQueryBuilder setPassportSeriesAndId(String passportSeriesAndId);

    public List<SupplementedUser> buildAndExecuteGetByFullNameAndStateAndNumberAndPassport();
}
