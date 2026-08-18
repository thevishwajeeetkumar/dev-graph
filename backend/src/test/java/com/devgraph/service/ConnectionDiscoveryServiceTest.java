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

import com.devgraph.dto.ConnectionDiscoveryResponse;
import com.devgraph.dto.ConnectionRecord;
import com.devgraph.exception.InvalidRequestException;
import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.model.Company;
import com.devgraph.model.Developer;
import com.devgraph.model.Project;
import com.devgraph.repository.ConnectionDiscoveryRepository;

@ExtendWith(MockitoExtension.class)
class ConnectionDiscoveryServiceTest {

    @Mock
    private ConnectionDiscoveryRepository connectionDiscoveryRepository;

    @InjectMocks
    private ConnectionDiscoveryService connectionDiscoveryService;

    @Test
    void discoverConnections_returnsConnections_forExistingDeveloper() {
        String developerId = "dev-1";
        ConnectionRecord record = new ConnectionRecord(
                new Developer("dev-2", "Ada", "Engineer", "Remote"),
                new Project("proj-1", "DevGraph", "Talent graph explorer"),
                List.of(new Company("co-1", "Acme")));

        when(connectionDiscoveryRepository.developerExists(developerId)).thenReturn(true);
        when(connectionDiscoveryRepository.findConnections(developerId, 0, 25)).thenReturn(List.of(record));

        ConnectionDiscoveryResponse response = connectionDiscoveryService.discoverConnections(developerId, null, null);

        assertThat(response.developerId()).isEqualTo(developerId);
        assertThat(response.connections()).containsExactly(record);
        assertThat(response.resultCount()).isEqualTo(1);
        assertThat(response.offset()).isZero();
        assertThat(response.limit()).isEqualTo(25);
    }

    @Test
    void discoverConnections_throwsNotFound_whenDeveloperMissing() {
        when(connectionDiscoveryRepository.developerExists("missing")).thenReturn(false);

        assertThatThrownBy(() -> connectionDiscoveryService.discoverConnections("missing", null, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void discoverConnections_throwsInvalidRequest_whenDeveloperIdBlank() {
        assertThatThrownBy(() -> connectionDiscoveryService.discoverConnections("   ", null, null))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void discoverConnections_throwsInvalidRequest_whenLimitOutOfRange() {
        assertThatThrownBy(() -> connectionDiscoveryService.discoverConnections("dev-1", null, 1000))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void discoverConnections_throwsInvalidRequest_whenOffsetNegative() {
        assertThatThrownBy(() -> connectionDiscoveryService.discoverConnections("dev-1", -1, null))
                .isInstanceOf(InvalidRequestException.class);
    }
}
