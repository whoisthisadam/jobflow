# Income Calculation Feature Implementation Plan

## Phase 1: Database and Backend (2-3 days)

### Database Changes
- [x] Create new `user_salary` table in the database schema with the following fields:
  - id (BIGINT, PRIMARY KEY)
  - user_id (BIGINT, FOREIGN KEY to app_user)
  - year (INTEGER)
  - month (INTEGER)
  - base_salary (FLOAT)
  - task_intensity_bonus (FLOAT)
  - experience_bonus (FLOAT)
  - income_tax (FLOAT)
  - cpp_tax (FLOAT)
  - other_deductions (FLOAT)
  - total_salary (FLOAT)
  - created_by (BIGINT, FOREIGN KEY to app_user)
  - created_at (TIMESTAMP)
  - updated_at (TIMESTAMP)
  - UNIQUE constraint on (user_id, year, month)

### Entity Creation
- [x] Create `UserSalary.java` entity class with fields matching the database table
- [x] Add appropriate annotations (@Entity, @Id, @ManyToOne, etc.)
- [x] Add getters, setters, and constructors

### Repository Creation
- [x] Create `UserSalaryRepository.java` interface extending JpaRepository
- [x] Add method to find salary by user, year, and month
- [x] Add method to find all salaries for a specific user

### Service Creation
- [x] Create `UserSalaryService.java` class
- [x] Implement method to save a new salary record
- [x] Implement method to find salary by user, year, and month
- [x] Implement method to update an existing salary record
- [x] Implement method to find all salaries for a specific user
- [x] Add validation logic for salary parameters

### DTO and Converter Creation
- [x] Create `UserSalaryDto.java` for transferring salary data
- [x] Create `UserSalaryToUserSalaryDtoConverter.java`
- [x] Create `UserSalaryDtoToUserSalaryConverter.java`

### Controller Creation
- [x] Create `UserSalaryController.java` with the following endpoints:
  - POST `/salary` - Create a new salary record (MANAGER role)
  - GET `/salary/user/{id}` - Get salary records for a specific user (MANAGER role)
  - GET `/salary/user/{id}/year/{year}/month/{month}` - Get specific salary record (MANAGER role)
  - GET `/salary/current` - Get salary records for the current user (USER role)
  - GET `/salary/current/year/{year}/month/{month}` - Get specific salary record for current user (USER role)

### Update Existing Services
- [x] Modify `UserService.findIncome(String date)` to check for salary records
- [x] Add logic to return special value/message when no salary record exists

## Phase 2: Frontend (2-3 days)

### Salary Management Component
- [x] Create `salary-management.component.html` with:
  - User selection dropdown
  - Year and month selection
  - Salary parameter inputs (base salary, bonuses, taxes, etc.)
  - Total salary calculation
  - Save button
- [x] Create `salary-management.component.ts` with:
  - Methods to handle form submission
  - Methods to calculate total salary
  - Methods to validate inputs

### Salary Service
- [x] Create `salary.service.ts` with:
  - Method to get salary data for a user
  - Method to save salary data for a user
  - Method to get salary data for the current user

### Update Account Component
- [x] Modify `account.component.ts` to handle the case when no salary data is available
- [x] Add logic to display a message when salary data is not yet set
- [x] Update the income display to show data from the new salary system

### Update Routing
- [x] Add route for the salary management component in `app.routes.ts`
- [x] Create module for the salary management component

### Update Navigation
- [x] Add link to the salary management page in the navigation menu (for managers only)

## Phase 3: Testing and Refinement (1-2 days)

### Manager Flow Testing
- [ ] Test creating new salary records
- [ ] Test viewing existing salary records
- [ ] Test updating existing salary records
- [ ] Test validation of salary parameters

### User Flow Testing
- [ ] Test viewing salary records as a user
- [ ] Test behavior when no salary record exists
- [ ] Test with different year/month combinations

### Edge Cases and Error Handling
- [x] Handle case when user has no category assigned
- [ ] Handle validation errors
- [ ] Add appropriate error messages
- [ ] Ensure proper security for all endpoints

### UI/UX Refinement
- [x] Improve form layout and styling
- [x] Add helpful information and tooltips
- [x] Pre-fill form with user's grade and calculated values
- [x] Add currency indicators and formatting
- [x] Add reset button for the form
- [ ] Add loading indicators
- [ ] Add confirmation messages
- [x] Ensure responsive design

## Additional Notes
- Ensure all new code follows existing project patterns and conventions
- Maintain proper security with role-based access control
- Document new API endpoints and components
- Consider adding pagination for users with many salary records
