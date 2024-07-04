import 'package:car_spotter/models/car_brands.dart';
import 'package:flutter/material.dart';

class CarBrandsDropdown extends StatefulWidget {
  const CarBrandsDropdown({super.key});

  @override
  State<CarBrandsDropdown> createState() {
    return _CarBrandsDropdownState();
  }
}

class _CarBrandsDropdownState extends State<CarBrandsDropdown> {
  final Map<String, List<String>> _carBrands = carBrands;
  String? _selectedBrand;
  String? _selectedModel;

  @override
  void initState() {
    super.initState();
    if (_carBrands.isNotEmpty) {
      _selectedBrand = _carBrands.keys.first;
      _selectedModel = _carBrands[_selectedBrand!]?.first;
    }
  }

  @override
  Widget build(BuildContext context) {
    final double screenWidth = MediaQuery.of(context).size.width;
    final double screenHeight = MediaQuery.of(context).size.height;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: EdgeInsets.only(left: screenWidth * 0.07),
          child: Text(
            'Brand',
            style: Theme.of(context).textTheme.titleLarge!.copyWith(
                  fontSize: 13,
                  color: const Color(0xFFDFA3A3),
                  fontWeight: FontWeight.w600,
                ),
          ),
        ),
        Container(
          margin: const EdgeInsets.only(
            top: 2,
          ),
          height: screenHeight * 0.06,
          decoration: const BoxDecoration(
            borderRadius: BorderRadius.all(
              Radius.circular(300),
            ),
            color: Color(0xFFD9D9D9),
          ),
          child: Padding(
            padding: EdgeInsets.only(
                left: screenWidth * 0.07, right: screenWidth * 0.09),
            child: DropdownButtonFormField(
              isExpanded: true,
              menuMaxHeight: screenHeight * 0.8575,
              icon: const ImageIcon(
                color: Color.fromARGB(255, 41, 41, 41),
                AssetImage("assets/images/icons/arrow-square-down.png"),
              ),
              iconSize: 30,
              decoration: const InputDecoration(
                border: InputBorder.none,
              ),
              style: Theme.of(context).textTheme.bodyLarge!.copyWith(
                    fontSize: 13,
                    fontWeight: FontWeight.w600,
                  ),
              value: _selectedBrand,
              onChanged: (String? newValue) {
                setState(() {
                  _selectedBrand = newValue;
                  _selectedModel = _carBrands[newValue]!.first;
                });
              },
              items: _carBrands.keys.map((String brand) {
                return DropdownMenuItem(
                  value: brand,
                  child: Text(brand),
                );
              }).toList(),
            ),
          ),
        ),
        SizedBox(height: screenHeight * 0.02),
        Padding(
          padding: EdgeInsets.only(left: screenWidth * 0.07),
          child: Text(
            'Model',
            style: Theme.of(context).textTheme.titleLarge!.copyWith(
                  fontSize: 13,
                  color: const Color(0xFFDFA3A3),
                  fontWeight: FontWeight.w600,
                ),
          ),
        ),
        Container(
          margin: const EdgeInsets.only(
            top: 2,
          ),
          height: screenHeight * 0.06,
          decoration: const BoxDecoration(
            borderRadius: BorderRadius.all(
              Radius.circular(300),
            ),
            color: Color(0xFFD9D9D9),
          ),
          child: Padding(
            padding: EdgeInsets.only(
                left: screenWidth * 0.07, right: screenWidth * 0.09),
            child: DropdownButtonFormField(
              isExpanded: true,
              menuMaxHeight: screenHeight * 0.8575,
              icon: const ImageIcon(
                color: Color.fromARGB(255, 41, 41, 41),
                AssetImage("assets/images/icons/arrow-square-down.png"),
              ),
              iconSize: 30,
              decoration: const InputDecoration(
                border: InputBorder.none,
              ),
              style: Theme.of(context).textTheme.bodyLarge!.copyWith(
                    fontSize: 13,
                    fontWeight: FontWeight.w600,
                  ),
              value: _selectedModel,
              onChanged: (String? newValue) {
                setState(() {
                  _selectedModel = newValue;
                });
              },
              items: _carBrands[_selectedBrand]!.map((String model) {
                return DropdownMenuItem(
                  value: model,
                  child: Text(model),
                );
              }).toList(),
            ),
          ),
        ),
      ],
    );
  }
}
