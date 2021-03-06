package portaltek.hexa.domain.svc.catalog;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import portaltek.hexa.domain.HexaException;
import portaltek.hexa.domain.dto.catalog.AccountDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static portaltek.hexa.domain.enums.ErrorType.ACCOUNT_CODE_DUPLICATED;
import static portaltek.hexa.domain.svc.catalog.ImportCatalogTestUtils.getValidAccount;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ImportCatalogCmdUnitTest {

    @Test
    public void givenRepeatedValidAccounts_whenDtoCreation_thenThrowException() throws HexaException {
        AccountDto a1 = getValidAccount(1L).code("100");
        AccountDto a2 = getValidAccount(2L).code("100");

        List<AccountDto> accountDtos = Stream.of(a1, a2).collect(Collectors.toList());

        HexaException exception = assertThrows(HexaException.class,
                () -> {
                    new ImportCatalogCmd(1L, 1L, accountDtos);
                });
        assertEquals(2, exception.by(ACCOUNT_CODE_DUPLICATED).size());

        log.info("givenRepeatedValidAccounts_whenDtoCreation_thenThrowException: PASSED");
    }

    @Test
    public void givenInvalidAccounts_whenDtoCreation_thenThrowException() throws HexaException {
        List<AccountDto> accountDtos = Stream.of(getValidAccount(1L), getValidAccount(2L))
                .map(i -> i.description(""))
                .collect(Collectors.toList());

        HexaException exception = assertThrows(HexaException.class,
                () -> {
                    new ImportCatalogCmd(1L, 1L, accountDtos);
                });

        //log.info("Errors: {}", exception.errors());
        log.info("givenInvalidAccounts_whenDtoCreation_thenThrowException: PASSED");
    }

    @Test
    public void givenInvalidCompany_whenDtoCreation_thenThrowException() {

        List<AccountDto> accountDtos = Stream.of(getValidAccount(1L)).collect(Collectors.toList());
        HexaException exception = assertThrows(HexaException.class,
                () -> {
                    new ImportCatalogCmd(null, 1L, accountDtos);
                });

        //log.info("Exception: {}", exception);
        log.info("givenInvalidCompany_whenDtoCreation_thenThrowException: PASSED");
    }
}