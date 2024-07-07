import 'package:car_spotter/models/user.dart';
import 'package:car_spotter/models/user_car.dart';
import 'package:car_spotter/ui/widgets/profile_customization_screen/name.dart';
import 'package:country_picker/country_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

final userLoginProfileProvider = ChangeNotifierProvider<UserProfile>((ref) => UserProfile());

class UserProfile with ChangeNotifier {
  String _firstName = '';
  String _lastName = '';
  String _country = '';
  Car? _car;

  String get firstName => _firstName;
  String get lastName => _lastName;
  String get country => _country;
  Car? get car => _car;

  void updateProfile({
    required String firstName,
    required String lastName,
    required String country,
  }) {
    _firstName = firstName;
    _lastName = lastName;
    _country = country;
    notifyListeners();
  }

  void updateCar({
    required String make,
    required String model,
    required int year,

  }) {
    _car = Car(make: make, model: model, year: year);
    notifyListeners();
  }

  User createUser(String username) {
    return User(
      username: username,
      firstName: _firstName,
      lastName: _lastName,
      country: _country,
      car: _car,
    );
  }
}