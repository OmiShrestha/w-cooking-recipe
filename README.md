# Recipe App

## Navigation 

### Navigation 

```plantuml
@startuml
[*] --> recipe_list

recipe_list --> favorites : heart button
recipe_list --> recipe_detail : tap recipe card

state "create_recipe_flow" as CreateFlow {
    [*] --> add_recipe_details
    add_recipe_details --> add_ingredients : next
    add_ingredients --> add_steps : next
}

recipe_list --> CreateFlow : FAB
add_recipe_details --> recipe_list : cancel
CreateFlow --> recipe_list : cancel
add_steps --> recipe_list : save

favorites --> recipe_list : back
recipe_detail --> recipe_list : back
@enduml
```

### Screens

| Screen | Function |
|--------|----------|
| Recipe list | Displays all recipes organized by category. Entry point for navigating to recipe detail screen, favorites screen, and the create recipe flow. |
| Favorites | Displays all recipes marked as favorites. Navigated to by selecting heart icon.|
| Recipe detail | Displays the full details of a selected recipe including ingredients and steps. |
| Add recipe details | Step 1 of the create recipe flow. Collects the recipe name and category. |
| Add ingredients | Step 2 of the create recipe flow. Collects the ingredients with name, quantity, and unit. |
| Add steps | Step 3 of the create recipe flow. Collects the preparation steps in order. This is where the user is finally given the option to save their new recipe.|

## Data Model

### Entity Relationship Diagram
![Entity Relationship Diagram](docs/erd.png)

#### Recipe
| Column | Type | Key |
|--------|------|-----|
| recipeId | Int | PK |
| name | String | |
| category | String | |
| isFavorite | Boolean | |

#### Ingredient
| Column | Type | Key |
|--------|------|-----|
| ingredientId | Int | PK |
| recipeId | Int | FK |
| name | String | |
| quantity | Double | |
| unit | String | |

#### Step
| Column | Type | Key |
|--------|------|-----|
| stepId | Int | PK |
| recipeId | Int | FK |
| sequenceNum | Int | |
| step | String | |
