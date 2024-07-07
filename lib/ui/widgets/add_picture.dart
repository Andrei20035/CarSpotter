import 'package:car_spotter/controllers/add_image_controller.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class AddPicture extends StatelessWidget {
  const AddPicture({super.key, required this.controller});

  final AddPictureController controller;

  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;

    return GestureDetector(
        onTap: () {
          controller.pickImageFromGallery();
        },
        child: AnimatedBuilder(
          animation: controller,
          builder: (context, _) {
            return Container(
              width: screenWidth * 0.5,
              height: screenWidth * 0.5,
              decoration: BoxDecoration(
                border: controller.selectedImage != null
                    ? Border.all(
                        color: const Color.fromARGB(255, 88, 88, 88),
                        width: 3,
                      )
                    : const Border.symmetric(),
                shape: BoxShape.circle,
                image: DecorationImage(
                  image: controller.selectedImage != null
                      ? FileImage(controller.selectedImage!) as ImageProvider
                      : const AssetImage('assets/images/placeholder.png'),
                  fit: BoxFit.cover,
                ),
              ),
            );
          },
        ));
  }
}
