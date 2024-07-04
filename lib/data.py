import requests
from bs4 import BeautifulSoup
import os

# Initial URL and headers
base_url = 'https://www.autoevolution.com/cars/'
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
}

# Path for the Dart file
dart_file_path = 'lib/models/car_brands.dart'

# Mapping for brand name exceptions
brand_url_exceptions = {
    "DeLorean": "dmc",
    "MARUSSIA" : "marussia-motors",
    "SSANGYONG" : "ssang-yong",
    "TESLA" : "tesla-motors"
}

try:
    # Send a request to the main page
    response = requests.get(base_url, headers=headers)
    response.raise_for_status()  # Raise an error for bad responses

    # Parse the HTML content using BeautifulSoup
    soup = BeautifulSoup(response.text, 'html.parser')

    # Find the element containing car brands
    brand_container = soup.find('div', class_='container carlist clearfix')

    if brand_container:
        # Extract all brand elements
        brand_elements = brand_container.find_all('span', itemprop="name")

        # Create a dictionary to hold the brand and models information
        brands = {}

        # Iterate through the brand elements to get each brand's models
        for brand_element in brand_elements:
            brand_name = brand_element.text.strip()

            # Check if the brand name has an exception in the mapping
            if brand_name in brand_url_exceptions:
                brand_slug = brand_url_exceptions[brand_name]
            else:
                brand_slug = brand_name.lower().replace(' ', '-')

            brand_url = f'https://www.autoevolution.com/{brand_slug}/'

            # Request each brand page to get the models
            try:
                brand_response = requests.get(brand_url, headers=headers)
                brand_response.raise_for_status()

                brand_soup = BeautifulSoup(brand_response.text, 'html.parser')
                model_elements = brand_soup.find_all('div', class_="carmodels col23width clearfix")

                # Extract models for the brand
                models = []
                for model_element in model_elements:
                    car_mods = model_element.find_all('h4')
                    for car_mod in car_mods:
                        model_name = car_mod.text.strip()
                        models.append(model_name)
                
                if not models:
                    models = ["No models found"]
                else:
                    models.sort()
                    
                brands[brand_name] = models

            except requests.exceptions.RequestException as e:
                print(f"Error fetching page for {brand_name}: {e}")
                brands[brand_name] = ["Error fetching models"]

        # Write the brands and their models to the Dart file
        with open(dart_file_path, "w") as dart_file:
            dart_file.write("Map<String, List<String>> carBrands = {\n")
            for brand, models in brands.items():
                dart_file.write(f'  "{brand.upper()}": [\n')
                for model in models:
                    dart_file.write(f'    "{model[len(brand) + 1:].upper()}",\n')
                dart_file.write("  ],\n")
            dart_file.write("};\n")
        print(f"Dart file '{dart_file_path}' was generated.")
    else:
        print("Brand container element not found.")
except requests.exceptions.RequestException as e:
    print(f"Error fetching page: {e}")
except Exception as e:
    print(f"An error occurred: {e}")
