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

import com.devgraph.dto.PathNode;
import com.devgraph.dto.ShortestPathResponse;
import com.devgraph.exception.InvalidRequestException;
import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.repository.ShortestPathRepository;

@ExtendWith(MockitoExtension.class)
class ShortestPathServiceTest {

    @Mock
    private ShortestPathRepository shortestPathRepository;

    @InjectMocks
    private ShortestPathService shortestPathService;

    @Test
    void findShortestPath_returnsPath_whenBothNodesExist() {
        String developerId = "dev-1";
        String companyId = "co-1";
        ShortestPathResponse expected = new ShortestPathResponse(
                true,
                List.of(new PathNode("Developer", developerId, "Ada"), new PathNode("Company", companyId, "Acme")),
                List.of("WORKED_AT"),
                1);

        when(shortestPathRepository.developerExists(developerId)).thenReturn(true);
        when(shortestPathRepository.companyExists(companyId)).thenReturn(true);
        when(shortestPathRepository.findShortestPath(developerId, companyId, 6)).thenReturn(expected);

        ShortestPathResponse response = shortestPathService.findShortestPath(developerId, companyId, null);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void findShortestPath_returnsNotFoundPath_whenNoConnectionExists() {
        String developerId = "dev-1";
        String companyId = "co-1";

        when(shortestPathRepository.developerExists(developerId)).thenReturn(true);
        when(shortestPathRepository.companyExists(companyId)).thenReturn(true);
        when(shortestPathRepository.findShortestPath(developerId, companyId, 6)).thenReturn(ShortestPathResponse.notFound());

        ShortestPathResponse response = shortestPathService.findShortestPath(developerId, companyId, null);

        assertThat(response.pathFound()).isFalse();
        assertThat(response.nodes()).isEmpty();
    }

    @Test
    void findShortestPath_throwsNotFound_whenDeveloperMissing() {
        when(shortestPathRepository.developerExists("missing")).thenReturn(false);

        assertThatThrownBy(() -> shortestPathService.findShortestPath("missing", "co-1", null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findShortestPath_throwsNotFound_whenCompanyMissing() {
        when(shortestPathRepository.developerExists("dev-1")).thenReturn(true);
        when(shortestPathRepository.companyExists("missing")).thenReturn(false);

        assertThatThrownBy(() -> shortestPathService.findShortestPath("dev-1", "missing", null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findShortestPath_throwsInvalidRequest_whenMaxHopsOutOfRange() {
        assertThatThrownBy(() -> shortestPathService.findShortestPath("dev-1", "co-1", 50))
                .isInstanceOf(InvalidRequestException.class);
    }
}
