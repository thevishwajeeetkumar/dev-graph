package com.devgraph.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devgraph.dto.ExpertPathResponse;
import com.devgraph.dto.PathNode;
import com.devgraph.exception.InvalidRequestException;
import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.model.Developer;
import com.devgraph.model.Skill;
import com.devgraph.repository.DeveloperRepository;
import com.devgraph.repository.ExpertDiscoveryRepository;
import com.devgraph.repository.ExpertDiscoveryRepository.NearestExpertPath;

@ExtendWith(MockitoExtension.class)
class ExpertDiscoveryServiceTest {

    @Mock
    private ExpertDiscoveryRepository expertDiscoveryRepository;

    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private ExpertDiscoveryService expertDiscoveryService;

    private static final Skill KAFKA = new Skill("skill-kafka", "Kafka");

    @Test
    void findNearestExpert_returnsSelf_whenDeveloperAlreadyHasSkill() {
        when(expertDiscoveryRepository.developerExists("dev-1")).thenReturn(true);
        when(expertDiscoveryRepository.findSkill("skill-kafka")).thenReturn(KAFKA);
        when(expertDiscoveryRepository.developerHasSkillDirectly("dev-1", "skill-kafka")).thenReturn(true);
        when(developerRepository.findById("dev-1")).thenReturn(Optional.of(new Developer("dev-1", "Ada", "Engineer", "Remote")));

        ExpertPathResponse response = expertDiscoveryService.findNearestExpert("dev-1", "skill-kafka", null);

        assertThat(response.pathFound()).isTrue();
        assertThat(response.hops()).isZero();
        assertThat(response.nodes()).containsExactly(new PathNode("Developer", "dev-1", "Ada"));
        assertThat(response.matchedSkill()).isEqualTo(KAFKA);
    }

    @Test
    void findNearestExpert_returnsNearestChain_whenSomeoneElseHasSkill() {
        NearestExpertPath path = new NearestExpertPath(
                List.of(new PathNode("Developer", "dev-1", "Ada"), new PathNode("Developer", "dev-2", "Chloe")),
                List.of("KNOWS"),
                1);

        when(expertDiscoveryRepository.developerExists("dev-1")).thenReturn(true);
        when(expertDiscoveryRepository.findSkill("skill-kafka")).thenReturn(KAFKA);
        when(expertDiscoveryRepository.developerHasSkillDirectly("dev-1", "skill-kafka")).thenReturn(false);
        when(expertDiscoveryRepository.findSkillHolderIds("dev-1", "skill-kafka")).thenReturn(List.of("dev-2"));
        when(expertDiscoveryRepository.findNearestExpertPath("dev-1", List.of("dev-2"), 6)).thenReturn(path);

        ExpertPathResponse response = expertDiscoveryService.findNearestExpert("dev-1", "skill-kafka", null);

        assertThat(response.pathFound()).isTrue();
        assertThat(response.hops()).isEqualTo(1);
        assertThat(response.matchedSkill()).isEqualTo(KAFKA);
    }

    @Test
    void findNearestExpert_returnsNotFound_whenNoOneElseHasSkill() {
        when(expertDiscoveryRepository.developerExists("dev-1")).thenReturn(true);
        when(expertDiscoveryRepository.findSkill("skill-kafka")).thenReturn(KAFKA);
        when(expertDiscoveryRepository.developerHasSkillDirectly("dev-1", "skill-kafka")).thenReturn(false);
        when(expertDiscoveryRepository.findSkillHolderIds("dev-1", "skill-kafka")).thenReturn(List.of());

        ExpertPathResponse response = expertDiscoveryService.findNearestExpert("dev-1", "skill-kafka", null);

        assertThat(response.pathFound()).isFalse();
        assertThat(response.matchedSkill()).isEqualTo(KAFKA);
    }

    @Test
    void findNearestExpert_returnsNotFound_whenNoChainWithinMaxHops() {
        when(expertDiscoveryRepository.developerExists("dev-1")).thenReturn(true);
        when(expertDiscoveryRepository.findSkill("skill-kafka")).thenReturn(KAFKA);
        when(expertDiscoveryRepository.developerHasSkillDirectly("dev-1", "skill-kafka")).thenReturn(false);
        when(expertDiscoveryRepository.findSkillHolderIds("dev-1", "skill-kafka")).thenReturn(List.of("dev-2"));
        when(expertDiscoveryRepository.findNearestExpertPath("dev-1", List.of("dev-2"), 6)).thenReturn(null);

        ExpertPathResponse response = expertDiscoveryService.findNearestExpert("dev-1", "skill-kafka", null);

        assertThat(response.pathFound()).isFalse();
    }

    @Test
    void findNearestExpert_throwsNotFound_whenDeveloperMissing() {
        when(expertDiscoveryRepository.developerExists("missing")).thenReturn(false);

        assertThatThrownBy(() -> expertDiscoveryService.findNearestExpert("missing", "skill-kafka", null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findNearestExpert_throwsNotFound_whenSkillMissing() {
        when(expertDiscoveryRepository.developerExists("dev-1")).thenReturn(true);
        when(expertDiscoveryRepository.findSkill("missing")).thenReturn(null);

        assertThatThrownBy(() -> expertDiscoveryService.findNearestExpert("dev-1", "missing", null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findNearestExpert_throwsInvalidRequest_whenMaxHopsOutOfRange() {
        assertThatThrownBy(() -> expertDiscoveryService.findNearestExpert("dev-1", "skill-kafka", 50))
                .isInstanceOf(InvalidRequestException.class);
    }
}
