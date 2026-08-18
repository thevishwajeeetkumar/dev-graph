package com.devgraph.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devgraph.dto.TalentBridgeRecord;
import com.devgraph.dto.TalentBridgeResponse;
import com.devgraph.exception.InvalidRequestException;
import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.model.Developer;
import com.devgraph.repository.TalentBridgeRepository;

@ExtendWith(MockitoExtension.class)
class TalentBridgeServiceTest {

    @Mock
    private TalentBridgeRepository talentBridgeRepository;

    @InjectMocks
    private TalentBridgeService talentBridgeService;

    @Test
    void discoverBridges_returnsBridges_forExistingCompanies() {
        String companyAId = "co-a";
        String companyBId = "co-b";
        TalentBridgeRecord record = new TalentBridgeRecord(
                new Developer("dev-1", "Ada", "Engineer", "Remote"),
                new Developer("dev-2", "Grace", "Architect", "Remote"),
                "KNOWS");

        when(talentBridgeRepository.companyExists(companyAId)).thenReturn(true);
        when(talentBridgeRepository.companyExists(companyBId)).thenReturn(true);
        when(talentBridgeRepository.findBridges(companyAId, companyBId, 0, 25)).thenReturn(List.of(record));

        TalentBridgeResponse response = talentBridgeService.discoverBridges(companyAId, companyBId, null, null);

        assertThat(response.bridges()).containsExactly(record);
        assertThat(response.resultCount()).isEqualTo(1);
    }

    @Test
    void discoverBridges_throwsInvalidRequest_whenCompaniesAreTheSame() {
        assertThatThrownBy(() -> talentBridgeService.discoverBridges("co-a", "co-a", null, null))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void discoverBridges_throwsNotFound_whenCompanyAMissing() {
        when(talentBridgeRepository.companyExists("co-a")).thenReturn(false);

        assertThatThrownBy(() -> talentBridgeService.discoverBridges("co-a", "co-b", null, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void discoverBridges_throwsNotFound_whenCompanyBMissing() {
        when(talentBridgeRepository.companyExists("co-a")).thenReturn(true);
        when(talentBridgeRepository.companyExists("co-b")).thenReturn(false);

        assertThatThrownBy(() -> talentBridgeService.discoverBridges("co-a", "co-b", null, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
