# Recipe App

## Data Model

### Entity Relationship Diagram
@startuml
entity "recipe" {
    * recipeId : INTEGER <<PK, Auto-generated>>
    --
    * name : TEXT
    * category : TEXT
    * isFavorite : INTEGER (Boolean)
}

entity "Ingredient" {
    * ingredientId : INTEGER <<PK, Auto-generated>>
    --
    * recipeId : INTEGER <<FK>>
    * name : TEXT
    * quantity : REAL
    * unit : TEXT
}

entity "Step" {
    * stepId : INTEGER <<PK, Auto-generated>>
    --
    * recipeId : INTEGER <<FK>>
    * sequenceNum : INTEGER
    * step : TEXT
}

entity "User" {
    * email : TEXT <<PK>>
    --
    * name : TEXT
    profilePictureUrl : TEXT
    * role : TEXT
}

recipe ||--o{ Ingredient : "recipeId"
recipe ||--o{ Step : "recipeId"
@enduml

### Database Schema

**recipe**

| Column | Type | Constraints |
|---|---|---|
| `recipeId` | INTEGER | Primary Key, Auto-generated |
| `name` | TEXT | Not Null |
| `category` | TEXT | Not Null |
| `isFavorite` | INTEGER (Boolean) | Not Null |

**Ingredient**

| Column | Type | Constraints |
|---|---|---|
| `ingredientId` | INTEGER | Primary Key, Auto-generated |
| `recipeId` | INTEGER | Foreign Key → `recipe.recipeId`, On Delete Cascade |
| `name` | TEXT | Not Null |
| `quantity` | REAL | Not Null |
| `unit` | TEXT | Not Null |

**Step**

| Column | Type | Constraints |
|---|---|---|
| `stepId` | INTEGER | Primary Key, Auto-generated |
| `recipeId` | INTEGER | Foreign Key → `recipe.recipeId`, On Delete Cascade |
| `sequenceNum` | INTEGER | Not Null |
| `step` | TEXT | Not Null |

**User**

| Column | Type | Constraints |
|---|---|---|
| `email` | TEXT | Primary Key |
| `name` | TEXT | Defaults to email if null|
| `profilePictureUrl` | TEXT, Nullable|
| `role` | TEXT | ADMIN/USER, Not Null |

---

## Navigation Graph

```plantuml
@startuml
state login
[*] --> login

login --> recipe_list : sign in success

recipe_list --> favorites : heart button
recipe_list --> recipe_detail : tap recipe card
recipe_list --> CreateFlow : FAB (admin only)

state "create_recipe_flow" as CreateFlow {
    [*] --> add_recipe_details
    add_recipe_details --> add_ingredients : next
    add_ingredients --> add_recipe_details : back
    add_ingredients --> add_steps : next
    add_steps --> add_ingredients : back
}

state "edit_recipe_flow" as EditFlow {
    [*] --> edit_recipe_details
    edit_recipe_details --> edit_add_ingredients : next
    edit_add_ingredients --> edit_recipe_details : back
    edit_add_ingredients --> edit_add_steps : next
    edit_add_steps --> edit_add_ingredients : back
    edit_add_steps --> recipe_detail : update recipe
    edit_recipe_details --> recipe_detail : cancel
}

recipe_detail --> EditFlow : edit button (admin only)
recipe_list --> api_search : search button
api_search --> api_meal_detail : tap result
recipe_list --> login : sign out
favorites --> recipe_list : back
recipe_detail --> recipe_list : back
@enduml
```

### Screens

| Screen | Function |
|---|---|
| `login` | Entry point of application for new users. Can be navigated to only on first time launch or after logging out. Succesful login navigates to recipe_list |
| `recipe_list` | Displays all recipes organized by category. Entry point for navigating to recipe detail, favorites, and the create recipe flow. |
| `favorites` | Displays all recipes marked as favorites. Navigated to by selecting the heart icon. |
| `recipe_detail` | Displays the full details of a selected recipe including ingredients and steps. |
| `add_recipe_details` | Step 1 of the create recipe flow. Collects the recipe name and category. |
| `add_ingredients` | Step 2 of the create recipe flow. Collects the ingredients with name, quantity, and unit. |
| `api_search_screen` | Key word search for recipes bases on name, category, or ingredient. Searching returns a list of recipes. |
| `api_meal_detail_screen` | Navigated to by selecting a meal in the api_search_screen. Presents meal recipe details with option to save recipe. |

# Note: All screens used to edit a saved recipe are reused screens from the add-recipe flow

---

## App Architecture

```plantuml
@startuml
skinparam componentStyle rectangle

package "UI Layer" {
    package "api_search_flow" {
        [ApiSearchScreen]
        [ApiMealDetailScreen]
    }

    [RecipeListScreen]
    [FavoritesScreen]
    [RecipeDetailScreen]
    [LoginScreen]
    package "create_recipe_flow (nested nav graph)" {
        [AddRecipeDetailsScreen]
        [AddIngredientsScreen]
        [AddStepsScreen]
    }
}

package "ViewModel Layer" {
    [RecipeViewModel] <<scoped to NavGraph>>
    [AddRecipeViewModel] <<scoped to create_recipe_flow>>
    [ApiSearchViewModel] <<scoped to ApiSearchScreen>>
    [ApiMealDetailViewModel] <<scoped to ApiMealDetailScreen>>
    [AuthViewModel] <<scoped to MainActivity>>
}

package "Data Layer" {
    [RecipeRepository]
    [RecipeRepositoryImpl]
    [AuthRepository]
    [AuthRepositoryImpl]
    [RecipeDao]
    [IngredientDao]
    [StepDao]
    [UserDao]
    database "RecipeDatabase" {
    }
}

package "Network Layer" {
    [MealRepository]
    [MealRepositoryImpl]
    [MealApiService]
    [RetrofitClient]
}

ApiSearchViewModel --> MealRepository
ApiMealDetailViewModel --> MealRepository
ApiMealDetailViewModel --> RecipeRepository
MealRepositoryImpl ..|> MealRepository
MealRepositoryImpl --> MealApiService
MealApiService --> RetrofitClient



AuthRepositoryImpl ..|> AuthRepository
AuthRepositoryImpl --> UserDao

RecipeListScreen --> RecipeViewModel
FavoritesScreen --> RecipeViewModel
RecipeDetailScreen --> RecipeViewModel
ApiSearchScreen --> ApiSearchViewModel
ApiMealDetailScreen --> ApiMealDetailViewModel
AddStepsScreen --> RecipeViewModel
AddRecipeDetailsScreen --> AddRecipeViewModel
AddIngredientsScreen --> AddRecipeViewModel
LoginScreen --> AuthViewModel
AddStepsScreen --> AddRecipeViewModel

RecipeViewModel --> RecipeRepository
AddRecipeViewModel ..> RecipeRepository : categories (static)

RecipeRepositoryImpl ..|> RecipeRepository
RecipeRepositoryImpl --> RecipeDao
RecipeRepositoryImpl --> IngredientDao
RecipeRepositoryImpl --> StepDao
AuthViewModel --> AuthRepository
UserDao --> RecipeDatabase

RecipeDao --> RecipeDatabase
IngredientDao --> RecipeDatabase
StepDao --> RecipeDatabase
@enduml
```

### Component Descriptions

| Component | Scope | Purpose |
|---|---|---|
| `RecipeListScreen` | UI | Displays all recipes; entry point for navigation to detail, favorites, and create flow |
| `FavoritesScreen` | UI | Displays recipes marked as favorite; allows toggling favorite off |
| `RecipeDetailScreen` | UI | Shows full details (ingredients and steps) of a selected recipe |
| `AddRecipeDetailsScreen` | UI | Step 1 of create flow — collects recipe name and category |
| `AddIngredientsScreen` | UI | Step 2 of create flow — collects ingredient name, quantity, and unit |
| `AddStepsScreen` | UI | Step 3 of create flow — collects cooking steps; submits the recipe |
| `RecipeViewModel` | Scoped to NavGraph | Shared across all screens; provides recipe/favorites streams and handles DB write operations |
| `AddRecipeViewModel` | Scoped to `create_recipe_flow` | Manages form state across the three create-recipe screens; cleared after submission |
| `RecipeRepository` | Data | Single source of truth for all data operations; abstracts DAOs from ViewModels |
| `RecipeDao` | Data | Room DAO for CRUD operations on the `recipe` table |
| `IngredientDao` | Data | Room DAO for CRUD operations on the `Ingredient` table |
| `StepDao` | Data | Room DAO for CRUD operations on the `Step` table |
| `RecipeDatabase` | Data | Room database; hosts all three tables and provides DAO instances |

---

## API Integration

The app integrates with [TheMealDB](https://www.themealdb.com/api.php) (free tier) to let users search for meals online and save them as local recipes.

### API Screens

| Screen | Function |
|---|---|
| `api_search` | Search by meal name, category, or ingredient. Displays results as a scrollable list of cards. |
| `api_meal_detail/{mealId}` | Shows full meal details fetched from the API: category, area, ingredients with measures, and numbered cooking steps. A FAB lets the user save the meal to the local database. |

### Network Layer

| Component | Purpose |
|---|---|
| `RetrofitClient` | Singleton Retrofit instance configured with base URL `https://www.themealdb.com/api/json/v1/1/` and a Gson converter. Includes an OkHttp logging interceptor for debug builds. |
| `MealApiService` | Retrofit service interface with suspend functions for `search.php` (by name), `filter.php` (by category/ingredient), and `lookup.php` (by ID). |
| `MealListResponse` | Wraps the `meals: List<MealDto>?` envelope returned by all API endpoints. |
| `MealDto` | Maps a full meal object from the API, including 20 ingredient/measure field pairs. Exposes a `getIngredients()` helper that zips non-empty ingredient names with their measures. |
| `MealRepository` | Interface that abstracts all API calls behind `Result`-returning suspend functions. |
| `MealRepositoryImpl` | Implements `MealRepository` using `runCatching` for uniform error handling. |

### API ViewModels

| ViewModel | Scope | Purpose |
|---|---|---|
| `ApiSearchViewModel` | `api_search` screen | Manages search query, `SearchType` (Name / Category / Ingredient), result list, loading, and error state. |
| `ApiMealDetailViewModel` | `api_meal_detail` screen | Fetches meal details by ID; handles converting and saving the API meal into local `Recipe`, `Ingredient`, and `Step` entities. |

### Saving an API Meal Locally

When the user taps **"Save Recipe"** on the the screen, `ApiMealDetailViewModel` converts the `MealDto` into local Room entities:

- A `Recipe` row is created from the meal name and category.
- Each non-empty ingredient/measure pair from `getIngredients()` becomes an `Ingredient` row.
- The raw instructions string is split on line breaks, blank lines and "Step N:" labels are stripped, and each remaining line becomes an ordered `Step` row.

## Testing

### Unit Tests
| Test Class | What it Tests |
|---|---|
| `RecipeViewModelTest` | Recipe stream, favorite toggling, and recipe insertion via fake repository |
| `AddRecipeViewModelTest` | Form state management, validation, and ingredient and step operations |
| `ApiSearchViewModelTest` | Search query handling, search type dispatch, successand failure states |

### Instrumented Tests (`src/androidTest/`)

#### DAO Tests
| Test Class | What it Tests |
|---|---|
| `RecipeDaoTest` | CRUD operations on the `recipe` table, favorite filtering, cascade delete |
| `IngredientDaoTest` | CRUD operations on the `Ingredient` table, per-recipe deletion |
| `StepDaoTest` | CRUD operations on the `Step` table, per ecipe deletion, step ordering |

#### UI Integration Tests
| Test Class | What it Tests |
|---|---|
| `ScreenIntegrationTest` | RecipeCard rendering, admin vs non admin visibility, form validation errors, ingredient and step row addition |
