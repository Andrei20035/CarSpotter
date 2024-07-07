import 'package:car_spotter/controllers/add_image_controller.dart';
import 'package:car_spotter/main.dart';
import 'package:car_spotter/providers/user_profile_provider.dart';
import 'package:car_spotter/ui/widgets/add_picture.dart';
import 'package:car_spotter/ui/widgets/profile_customization_screen/country_picker.dart';
import 'package:car_spotter/ui/widgets/login_button.dart';
import 'package:car_spotter/ui/widgets/profile_customization_screen/name.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class ProfileCustomization extends ConsumerStatefulWidget {
  const ProfileCustomization({super.key});

  @override
  ConsumerState<ProfileCustomization> createState() => _ProfileCustomizationState();
}

class _ProfileCustomizationState extends ConsumerState<ProfileCustomization> {
  final _formKey = GlobalKey<FormState>();
  late TextEditingController firstNameController;
  late TextEditingController lastNameController;
  late AddPictureController profilePictureController;
  late String selectedCountry;

  @override
  void initState() {
    super.initState();
    firstNameController = TextEditingController();
    lastNameController = TextEditingController();
    profilePictureController = AddPictureController();
  }

  @override
  void dispose() {
    super.dispose();
    profilePictureController.dispose();
    firstNameController.dispose();
    lastNameController.dispose();
  }

  void _unfocusTextFields() {
    FocusManager.instance.primaryFocus?.unfocus();
  }

  @override
  Widget build(BuildContext context) {
    final screenHeight = MediaQuery.of(context).size.height;
    final screenWidth = MediaQuery.of(context).size.width;

    return Scaffold(
      body: GestureDetector(
        onTap: _unfocusTextFields,
        child: Container(
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
                  child: Form(
                    key: _formKey,
                    child: Consumer(
                      builder: (context, watch, _) {
                        final userProfile = ref.watch(userLoginProfileProvider);
                      return Column(
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
                          AddPicture(controller: profilePictureController),
                          SizedBox(height: screenHeight * 0.07125),
                          FirstLastName(
                            controller: firstNameController,
                            text: "First name",
                            onEditingComplete: _unfocusTextFields,
                          ),
                          SizedBox(height: screenHeight * 0.02),
                          FirstLastName(
                            controller: lastNameController,
                            text: "Last name",
                            onEditingComplete: _unfocusTextFields,
                          ),
                          SizedBox(height: screenHeight * 0.02),
                          CountryPicker(onCountrySelected: (country) {
                            selectedCountry = country;
                          },),
                          SizedBox(height: screenHeight * 0.08),
                          LoginButton(
                            color: const Color(0xFFF0AB25),
                            onPressed: () {
                              if(_formKey.currentState?.validate() ?? false) {
                                userProfile.updateProfile(firstName: firstNameController.text, lastName: lastNameController.text, country: selectedCountry);
                              }
                              Navigator.pushNamed(context, '/yourCar');
                            },
                            text: "Next",
                          )
                        ],
                      );
                    
                      },
                  ),
                ),
              ),
            ),
          ),
        ),
      ),),);
    
  }
}
