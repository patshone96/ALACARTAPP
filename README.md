# ALACARTAPP

Final project of a 2-years-long course centered in learning programming, and specifically multiplatform apps (e.g. mobile apps, adaptive web design...) 

## Goal 

  This project aims to provide a free app where different restaurants configure their menus, adding diffeent dishes and beverages, 
  and then providing a list of ingredients and allergens. These configurations then translate into a dinamic menu rendered by a mobile app
  which accesses a database where the configurations are stored. 

## Project Structure 

### Mobile App

  The mobile appc consists of 3 activities: 
  
  1. The first activity let's you select a restaurant and serves as a savescreen while charging the data 
  
  2. The second activity shows a list/ grid of the different dishes, with the name of the restaurant on top and a drawer widget 
      which contains the different filters to apply to the menu. 
      
  3. The last screen appears when a dish is selected. It is dynamically rendered using the data of the dish and shows the picture, 
      the price, allergens, ingredients and a description of the dish. On back press, you return to the second activity. 
