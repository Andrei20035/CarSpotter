import 'package:car_spotter/main.dart';
import 'package:flutter/material.dart';
import 'package:country_picker/country_picker.dart';

class CountryPicker extends StatefulWidget {
  const CountryPicker({super.key});

  @override
  State<CountryPicker> createState() => _CountryPickerState();
}

class _CountryPickerState extends State<CountryPicker> {
  late String countryValue;
  late String flag;
  @override
  void initState() {
    super.initState();
    countryValue = '';
    flag = '';
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
            "Country",
            style: theme.textTheme.titleLarge!.copyWith(
              fontSize: 13,
              color: const Color(0xFFDFA3A3),
              fontWeight: FontWeight.w600,
            ),
          ),
        ),
        GestureDetector(
          onTap: () {
            showCountryPicker(
              context: context,
              onSelect: (Country country) {
                setState(() {
                  countryValue = country.name;
                  flag = country.flagEmoji;
                });
              },
              countryListTheme: CountryListThemeData(
                backgroundColor: const Color(0xFFD9D9D9),
                textStyle: theme.textTheme.bodyMedium!.copyWith(
                  fontSize: 15,
                  color: const Color(0xFF434343),
                ),
                inputDecoration: const InputDecoration(
                  labelText: 'Search',
                  prefixIcon: Icon(Icons.search),
                ),
                searchTextStyle: theme.textTheme.bodyMedium!.copyWith(
                  fontSize: 15,
                  color: const Color(0xFF434343),
                ),
              ),
            );
          },
          child: Container(
            margin: const EdgeInsets.only(
              top: 2,
            ),
            height: screenHeight * 0.06,
            width: double.infinity,
            decoration: const BoxDecoration(
              borderRadius: BorderRadius.all(
                Radius.circular(300),
              ),
              color: Color(0xFFD9D9D9),
            ),
            child: Padding(
              padding: EdgeInsets.symmetric(horizontal: screenWidth * 0.07),
              child: Row(
                children: [
                  Text(
                    flag,
                    style: const TextStyle(fontSize: 20),
                  ),
                  const SizedBox(width: 8),
                  Text(
                    countryValue,
                    style: theme.textTheme.bodyMedium!.copyWith(
                      color: const Color(0xFF434343),
                      fontSize: 13,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  const Spacer(),
                  const ImageIcon(
                    AssetImage(
                      "assets/images/icons/arrow-square-down.png",
                    ),
                    size: 30,
                  ),
                ],
              ),
            ),
          ),
        ),
      ],
    );
  }
}
