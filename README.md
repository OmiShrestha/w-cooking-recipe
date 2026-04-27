# Recipe App

## Navigation Graph

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

## Database Schema
![Entity Relationship Diagram](docs/erd.png)


## Screens

| Screen | Function |
|--------|----------|
| Recipe list | Displays all recipes organised by category. Entry point for navigating to recipe detail, favorites, and the create recipe flow. |
| Favorites | Displays all recipes marked as favorites. |
| Recipe detail | Displays the full details of a selected recipe including ingredients and steps. |
| Add recipe details | Step 1 of the create recipe flow. Collects the recipe name and category. |
| Add ingredients | Step 2 of the create recipe flow. Collects the ingredients with name, quantity, and unit. |
| Add steps | Step 3 of the create recipe flow. Collects the preparation steps in order. |