package com.revature.maadcars.services;

import com.revature.maadcars.models.Model;
import com.revature.maadcars.repository.ModelRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModelServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceTest.class);

    Model modelMock;
    List<Model> modelsMock;
    ModelRepository modelRepositoryMock;

    ModelService service;

    @BeforeAll
    static void beforeAll() {
        logger.trace("Now running VehicleService unit tests...");
    }

    @BeforeEach
    void setUp() {
        modelMock = Mockito.mock(Model.class);
        modelsMock = new ArrayList<>();
        modelsMock.add(modelMock);
        modelRepositoryMock = Mockito.mock(ModelRepository.class);

        service = new ModelService(modelRepositoryMock);
    }

    @Test
    void saveModel() {
        when(modelRepositoryMock.save(any(Model.class))).thenReturn(modelMock);

        Model model = service.saveModel(new Model());

        assertEquals(modelMock, model);
    }

    @Test
    void getModelByModelId() {
        when(modelRepositoryMock.findById(anyInt())).thenReturn(Optional.ofNullable(modelMock));

        Model model = service.getModelByModelId(1);

        assertEquals(modelMock, model);
    }

    @Test
    void getAllModels() {
        when(modelRepositoryMock.findAll()).thenReturn(modelsMock);

        List<Model> models = service.getAllModels();

        assertEquals(modelsMock, models);
    }

    @Test
    void deleteModel() {
        when(modelRepositoryMock.findById(anyInt())).thenReturn(Optional.of(modelMock));

        service.deleteModel(1);

        verify(modelRepositoryMock).delete(modelMock);
    }
}