import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';

class AddPictureController extends ChangeNotifier {
  File? _selectedImage;
  File? get selectedImage => _selectedImage;

  Future<void> pickImageFromGallery() async {
    final returnedImage = await ImagePicker().pickImage(
      source: ImageSource.gallery,
      maxHeight: 1000,
      maxWidth: 1000,
    );

    if (returnedImage != null) {
      _selectedImage = File(returnedImage.path);
      notifyListeners();
    }
  }
}
