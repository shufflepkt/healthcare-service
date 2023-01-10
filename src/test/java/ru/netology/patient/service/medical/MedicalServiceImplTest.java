package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

public class MedicalServiceImplTest {
    @MethodSource("methodSource1")
    @ParameterizedTest
    public void checkBloodPressureTest(String patientId, BloodPressure bloodPressure, int numberOfMessages) {
        PatientInfo patientInfo = new PatientInfo("1", "Ivan", "Ivanov",
                LocalDate.of(1991, Month.JANUARY, 15),
                new HealthInfo(new BigDecimal(36.6), new BloodPressure(120, 80)));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString()))
                .thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);


        medicalService.checkBloodPressure(patientId, bloodPressure);


        Mockito.verify(sendAlertService, Mockito.times(numberOfMessages)).send(Mockito.any());
    }

    public static Stream<Arguments> methodSource1() {
        return Stream.of(
                Arguments.of("1", new BloodPressure(120, 80), 0),
                Arguments.of("1", new BloodPressure(125, 85), 1)
        );
    }

    @MethodSource("methodSource2")
    @ParameterizedTest
    public void checkTemperatureTest(String patientId, BigDecimal temperature, int numberOfMessages) {
        PatientInfo patientInfo = new PatientInfo("1", "Ivan", "Ivanov",
                LocalDate.of(1991, Month.JANUARY, 15),
                new HealthInfo(new BigDecimal(36.6), new BloodPressure(120, 80)));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString()))
                .thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);


        medicalService.checkTemperature(patientId, temperature);


        Mockito.verify(sendAlertService, Mockito.times(numberOfMessages)).send(Mockito.any());
    }

    public static Stream<Arguments> methodSource2() {
        return Stream.of(
                Arguments.of("1", new BigDecimal("36.8"), 0),
                Arguments.of("1", new BigDecimal("35.1"), 1)
        );
    }

    @MethodSource("methodSource3")
    @ParameterizedTest
    public void checkBloodPressureTestCaptor(String patientId, BloodPressure bloodPressure) {
        PatientInfo patientInfo = new PatientInfo("1", "Ivan", "Ivanov",
                LocalDate.of(1991, Month.JANUARY, 15),
                new HealthInfo(new BigDecimal(36.6), new BloodPressure(120, 80)));

        String expected = "Warning, patient with id: 1, need help";

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString()))
                .thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        ArgumentCaptor<String> actual = ArgumentCaptor.forClass(String.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkBloodPressure(patientId, bloodPressure);

        Mockito.verify(sendAlertService).send(actual.capture());

        Assertions.assertEquals(expected, actual.getValue());
    }

    public static Stream<Arguments> methodSource3() {
        return Stream.of(
                Arguments.of("1", new BloodPressure(110, 70)),
                Arguments.of("1", new BloodPressure(125, 85))
        );
    }
}