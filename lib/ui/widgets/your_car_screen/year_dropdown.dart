import 'package:flutter/material.dart';

class YearDropdown extends StatefulWidget {
  const YearDropdown({super.key});

  @override
  State<YearDropdown> createState() => _YearDropdownState();
}

class _YearDropdownState extends State<YearDropdown> {
  int? _selectedYear;
  final List<int> range = List.generate(2024 - 1900 + 1, (index) => 1900 + index);

  @override
  void initState() {
    super.initState();
    range.sort(((a, b) => b.compareTo(a)));
    _selectedYear = range.first;
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
            'Year',
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
              value: _selectedYear,
              onChanged: (int? newYear) {
                setState(() {
                  _selectedYear = newYear;
                });
              },
              items: range.map((int brand) {
                return DropdownMenuItem(
                  value: brand,
                  child: Text(brand.toString()),
                );
              }).toList(),
            ),
          ),
        ),
      ],
    );
  }
}