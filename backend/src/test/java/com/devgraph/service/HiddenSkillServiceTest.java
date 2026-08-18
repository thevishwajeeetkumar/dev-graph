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

import com.devgraph.dto.HiddenSkillRecord;
import com.devgraph.dto.HiddenSkillResponse;
import com.devgraph.exception.InvalidRequestException;
import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.model.Project;
import com.devgraph.model.Skill;
import com.devgraph.repository.HiddenSkillRepository;

@ExtendWith(MockitoExtension.class)
class HiddenSkillServiceTest {

    @Mock
    private HiddenSkillRepository hiddenSkillRepository;

    @InjectMocks
    private HiddenSkillService hiddenSkillService;

    @Test
    void discoverHiddenSkills_returnsSkills_forExistingDeveloper() {
        String developerId = "dev-1";
        HiddenSkillRecord record = new HiddenSkillRecord(
                new Skill("skill-1", "Kotlin"),
                List.of(new Project("proj-1", "DevGraph", "Talent graph explorer")));

        when(hiddenSkillRepository.developerExists(developerId)).thenReturn(true);
        when(hiddenSkillRepository.findHiddenSkills(developerId, 0, 25)).thenReturn(List.of(record));

        HiddenSkillResponse response = hiddenSkillService.discoverHiddenSkills(developerId, null, null);

        assertThat(response.hiddenSkills()).containsExactly(record);
        assertThat(response.resultCount()).isEqualTo(1);
    }

    @Test
    void discoverHiddenSkills_throwsNotFound_whenDeveloperMissing() {
        when(hiddenSkillRepository.developerExists("missing")).thenReturn(false);

        assertThatThrownBy(() -> hiddenSkillService.discoverHiddenSkills("missing", null, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void discoverHiddenSkills_throwsInvalidRequest_whenOffsetNegative() {
        assertThatThrownBy(() -> hiddenSkillService.discoverHiddenSkills("dev-1", -1, null))
                .isInstanceOf(InvalidRequestException.class);
    }
}
