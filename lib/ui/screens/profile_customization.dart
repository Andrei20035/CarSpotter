import 'package:car_spotter/main.dart';
import 'package:car_spotter/ui/widgets/add_picture.dart';
import 'package:car_spotter/ui/widgets/login_button.dart';
import 'package:car_spotter/ui/widgets/text_input_field.dart';
import 'package:flutter/material.dart';

class ProfileCustomization extends StatelessWidget {
  const ProfileCustomization({super.key});

  @override
  Widget build(BuildContext context) {
    final screenHeight = MediaQuery.of(context).size.height;
    final screenWidth = MediaQuery.of(context).size.width;

    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [
              Color(0xFF000000),
              Color(0xFF080C30),
            ],
            stops: [
              0.0,
              0.24,
            ],
          ),
        ),
        child: Align(
          alignment: Alignment.topCenter,
          child: SizedBox(
            child: SingleChildScrollView(
              child: Padding(
                padding: EdgeInsets.symmetric(horizontal: screenWidth * 0.06),
                child: Column(
                  children: [
                    SizedBox(height: screenHeight * 0.12375),
                    Text(
                      "Your profile picture",
                      style: theme.textTheme.bodyLarge!.copyWith(
                          color: const Color(0xFFDFA3A3),
                          fontSize: 20,
                          fontWeight: FontWeight.w600),
                    ),
                    SizedBox(height: screenHeight * 0.02875),
                    const AddPicture(),
                    SizedBox(height: screenHeight * 0.07125),
                    TextInputField(
                      controller: TextEditingController(),
                      text: "First name",
                      titleColor: const Color(0xFFDFA3A3),
                    ),
                    SizedBox(height: screenHeight * 0.02),
                    TextInputField(
                      controller: TextEditingController(),
                      text: "Last name",
                      titleColor: const Color(0xFFDFA3A3),
                    ),
                    SizedBox(height: screenHeight * 0.02),
                    // const CountryPicker(),
                    SizedBox(height: screenHeight * 0.08),
                    LoginButton(
                      color: const Color(0xFFF0AB25),
                      onPressed: () {
                        Navigator.pushNamed(context, '/yourCar');
                      },
                      text: "Next",
                    )
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
