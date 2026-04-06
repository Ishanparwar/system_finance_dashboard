package com.zorvyn.financeSystem;

import com.zorvyn.financeSystem.dto.*;
import com.zorvyn.financeSystem.exception.BadRequestException;
import com.zorvyn.financeSystem.exception.ResourceNotFoundException;
import com.zorvyn.financeSystem.exception.UnauthorizedException;
import com.zorvyn.financeSystem.model.FinancialRecord;
import com.zorvyn.financeSystem.model.User;
import com.zorvyn.financeSystem.model.enums.RecordType;
import com.zorvyn.financeSystem.model.enums.Role;
import com.zorvyn.financeSystem.model.enums.Status;
import com.zorvyn.financeSystem.repository.FinancialRecordRepository;
import com.zorvyn.financeSystem.repository.UserRepository;
import com.zorvyn.financeSystem.service.impl.RecordServiceImpl;
import com.zorvyn.financeSystem.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class FinanceSystemApplicationTests {

	@Test
	void contextLoads() {
	}

	@Mock
	private UserRepository userRepository;

	@Mock
	private FinancialRecordRepository recordRepository;

	@InjectMocks
	private RecordServiceImpl recordService;

	@InjectMocks
	private UserServiceImpl userService;

	@Test
	void viewerShouldNotCreateRecord() {

		User viewer = User.builder()
				.id(1L)
				.role(Role.VIEWER)
				.status(Status.ACTIVE)
				.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(viewer));

		CreateRecordRequest request = new CreateRecordRequest();

		assertThrows(UnauthorizedException.class, () ->
				recordService.createRecord(request, 1L)
		);
	}

	@Test
	void inactiveUserShouldNotCreateRecord() {

		User user = User.builder()
				.id(1L)
				.role(Role.ANALYST)
				.status(Status.INACTIVE)
				.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		CreateRecordRequest request = new CreateRecordRequest();

		assertThrows(UnauthorizedException.class, () ->
				recordService.createRecord(request, 1L)
		);
	}

	@Test
	void shouldThrowIfStartDateAfterEndDate() {

		assertThrows(BadRequestException.class, () ->
				recordService.getAllRecords(
						null,
						null,
						LocalDate.of(2026, 4, 10),
						LocalDate.of(2026, 4, 1)
				)
		);
	}

	@Test
	void adminCanDeleteRecord() {

		User admin = User.builder()
				.id(1L)
				.role(Role.ADMIN)
				.status(Status.ACTIVE)
				.build();

		FinancialRecord record = FinancialRecord.builder()
				.id(1L)
				.isDeleted(false)
				.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
		when(recordRepository.findById(1L)).thenReturn(Optional.of(record));

		recordService.deleteRecord(1L, 1L);

		assertTrue(record.isDeleted());
		verify(recordRepository).save(record);
	}

	@Test
	void analystCannotDeleteRecord() {

		User analyst = User.builder()
				.id(1L)
				.role(Role.ANALYST)
				.status(Status.ACTIVE)
				.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(analyst));

		assertThrows(UnauthorizedException.class, () ->
				recordService.deleteRecord(1L, 1L)
		);
	}

	@Test
	void shouldThrowIfRecordAlreadyDeleted() {

		User admin = User.builder()
				.id(1L)
				.role(Role.ADMIN)
				.status(Status.ACTIVE)
				.build();

		FinancialRecord record = FinancialRecord.builder()
				.id(1L)
				.isDeleted(true)
				.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
		when(recordRepository.findById(1L)).thenReturn(Optional.of(record));

		assertThrows(ResourceNotFoundException.class, () ->
				recordService.deleteRecord(1L, 1L)
		);
	}

	@Test
	void analystCanCreateRecord() {

		User analyst = User.builder()
				.id(1L)
				.role(Role.ANALYST)
				.status(Status.ACTIVE)
				.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(analyst));
		when(recordRepository.save(any())).thenAnswer(i -> i.getArgument(0));

		CreateRecordRequest request = new CreateRecordRequest();
		request.setAmount(1000.0);

		RecordResponse response = recordService.createRecord(request, 1L);

		assertNotNull(response);
	}

	@Test
	void shouldUpdateOnlyProvidedFields() {

		User analyst = User.builder()
				.id(1L)
				.role(Role.ANALYST)
				.status(Status.ACTIVE)
				.build();

		User creator = User.builder()
				.id(2L)
				.build();

		FinancialRecord record = FinancialRecord.builder()
				.id(1L)
				.amount(1000.0)
				.category("FOOD")
				.createdBy(creator)
				.isDeleted(false)
				.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(analyst));
		when(recordRepository.findById(1L)).thenReturn(Optional.of(record));
		when(recordRepository.save(any())).thenAnswer(i -> i.getArgument(0));

		UpdateRecordRequest request = new UpdateRecordRequest();
		request.setAmount(2000.0);

		RecordResponse response = recordService.updateRecord(1L, request, 1L);

		assertEquals(2000.0, response.getAmount());
	}

	@Test
	void viewerCannotUpdateRecord() {

		User viewer = User.builder()
				.id(1L)
				.role(Role.VIEWER)
				.status(Status.ACTIVE)
				.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(viewer));

		assertThrows(UnauthorizedException.class, () ->
				recordService.updateRecord(1L, new UpdateRecordRequest(), 1L)
		);
	}

	@Test
	void shouldReturnFilteredRecords() {

		User creator = User.builder()
				.id(10L)
				.build();

		FinancialRecord record = FinancialRecord.builder()
				.id(1L)
				.amount(1000.0)
				.type(RecordType.INCOME)
				.category("SALARY")
				.date(LocalDate.now())
				.createdBy(creator)
				.isDeleted(false)
				.build();

		when(recordRepository.filterRecords(any(), any(), any(), any()))
				.thenReturn(List.of(record));

		List<RecordResponse> result =
				recordService.getAllRecords(null, null, null, null);

		assertEquals(1, result.size());
	}

	@Test
	void shouldFailIfFirstUserNotAdmin() {

		when(userRepository.count()).thenReturn(0L);

		CreateUserRequest request = new CreateUserRequest();
		request.setRole(Role.VIEWER);

		assertThrows(BadRequestException.class, () ->
				userService.createUser(request, null)
		);
	}

	@Test
	void nonAdminCannotCreateUser() {

		when(userRepository.count()).thenReturn(1L);

		User analyst = User.builder()
				.id(1L)
				.role(Role.ANALYST)
				.status(Status.ACTIVE)
				.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(analyst));

		assertThrows(UnauthorizedException.class, () ->
				userService.createUser(new CreateUserRequest(), 1L)
		);
	}

	@Test
	void shouldFailIfEmailAlreadyExists() {

		when(userRepository.count()).thenReturn(1L);

		User admin = User.builder()
				.id(1L)
				.role(Role.ADMIN)
				.status(Status.ACTIVE)
				.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
		when(userRepository.findByEmail("test@test.com"))
				.thenReturn(Optional.of(new User()));

		CreateUserRequest request = new CreateUserRequest();
		request.setEmail("test@test.com");

		assertThrows(BadRequestException.class, () ->
				userService.createUser(request, 1L)
		);
	}
	@Test
	void inactiveUserCannotUpdateRecord() {

		User user = User.builder()
				.id(1L)
				.role(Role.ANALYST)
				.status(Status.INACTIVE)
				.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		assertThrows(UnauthorizedException.class, () ->
				recordService.updateRecord(1L, new UpdateRecordRequest(), 1L)
		);
	}

	@Test
	void shouldCreateFirstAdminSuccessfully() {

		when(userRepository.count()).thenReturn(0L);
		when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

		CreateUserRequest request = new CreateUserRequest();
		request.setName("Admin");
		request.setEmail("admin@test.com");
		request.setRole(Role.ADMIN);

		UserResponse response = userService.createUser(request, null);

		assertEquals(Role.ADMIN, response.getRole());
	}

	@Test
	void inactiveAdminCannotCreateUser() {

		when(userRepository.count()).thenReturn(1L);

		User admin = User.builder()
				.id(1L)
				.role(Role.ADMIN)
				.status(Status.INACTIVE)
				.build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

		assertThrows(UnauthorizedException.class, () ->
				userService.createUser(new CreateUserRequest(), 1L)
		);
	}
}
