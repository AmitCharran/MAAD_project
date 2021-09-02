package com.revature.maadcars.services;

import com.revature.maadcars.models.Make;
import com.revature.maadcars.repository.MakeRepository;
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

class MakeServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceTest.class);

    Make makeMock;
    List<Make> makesMock;
    MakeRepository makeRepositoryMock;

    MakeService service;

    @BeforeAll
    static void beforeAll() {
        logger.trace("Now running VehicleService unit tests...");
    }

    @BeforeEach
    void setUp() {
        makeMock = Mockito.mock(Make.class);
        makesMock = new ArrayList<>();
        makesMock.add(makeMock);
        makeRepositoryMock = Mockito.mock(MakeRepository.class);

        service = new MakeService(makeRepositoryMock);
    }

    @Test
    void saveMake() {
        when(makeRepositoryMock.save(any(Make.class))).thenReturn(makeMock);

        Make make = service.saveMake(new Make());

        assertEquals(makeMock, make);
    }

    @Test
    void getMakeByMakeId() {
        when(makeRepositoryMock.findById(anyInt())).thenReturn(Optional.ofNullable(makeMock));

        Make make = service.getMakeByMakeId(1);

        assertEquals(makeMock, make);
    }

    @Test
    void getAllMakes() {
        when(makeRepositoryMock.findAll()).thenReturn(makesMock);

        List<Make> makes = service.getAllMakes();

        assertEquals(makesMock, makes);
    }

    @Test
    void deleteMake() {
        when(makeRepositoryMock.findById(anyInt())).thenReturn(Optional.of(makeMock));

        service.deleteMake(1);

        verify(makeRepositoryMock).delete(makeMock);
    }
}