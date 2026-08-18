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

import com.devgraph.exception.InvalidRequestException;
import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.model.Developer;
import com.devgraph.repository.DeveloperRepository;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DeveloperService developerService;

    @Test
    void getAllDevelopers_returnsRepositoryResult() {
        Developer developer = new Developer("dev-1", "Ada", "Engineer", "Remote");
        when(developerRepository.findAll()).thenReturn(List.of(developer));

        List<Developer> result = developerService.getAllDevelopers();

        assertThat(result).containsExactly(developer);
    }

    @Test
    void getDeveloper_returnsDeveloper_whenFound() {
        Developer developer = new Developer("dev-1", "Ada", "Engineer", "Remote");
        when(developerRepository.findById("dev-1")).thenReturn(Optional.of(developer));

        Developer result = developerService.getDeveloper("dev-1");

        assertThat(result).isEqualTo(developer);
    }

    @Test
    void getDeveloper_throwsNotFound_whenMissing() {
        when(developerRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> developerService.getDeveloper("missing"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getDeveloper_throwsInvalidRequest_whenIdBlank() {
        assertThatThrownBy(() -> developerService.getDeveloper(" "))
                .isInstanceOf(InvalidRequestException.class);
    }
}
