import 'package:car_spotter/providers/user_profile_provider.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class FirstLastName extends ConsumerWidget {
  const FirstLastName({
    super.key,
    required this.controller,
    required this.text,
    this.cursorColor = const Color(0xFF434343),
    this.titleColor = const Color(0xFFDFA3A3),
    this.keyboardType = TextInputType.text,
    this.focusNode,
    this.onEditingComplete
  });

  final Color cursorColor;
  final Color titleColor;
  final TextEditingController controller;
  final String text;
  final TextInputType keyboardType;
  final FocusNode? focusNode;
  final VoidCallback? onEditingComplete;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final screenHeight = MediaQuery.of(context).size.height;
    final screenWidth = MediaQuery.of(context).size.width;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: EdgeInsets.only(left: screenWidth * 0.07),
          child: Text(
            text,
            style:Theme.of(context).textTheme.titleLarge!.copyWith(
              fontSize: 13,
              color: titleColor,
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
            padding: EdgeInsets.symmetric(horizontal: screenWidth * 0.07),
            child: TextFormField(
              // Alti parametri de specificat
              focusNode: focusNode,
              controller: controller,
              style:Theme.of(context).textTheme.bodyLarge!.copyWith(
                color: cursorColor,
                fontSize: 13,
                fontWeight: FontWeight.w600,
              ),
              cursorColor: cursorColor,
              keyboardType: keyboardType,
              autocorrect: false,
              textCapitalization: TextCapitalization.none,
              decoration: InputDecoration(
                fillColor: cursorColor,
                border: InputBorder.none,
              ),
              onEditingComplete: onEditingComplete,
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return 'Please enter your ${text.toLowerCase()}';
                }
                return null;
              },
            
            ),
          ),
        ),
      ],
    );
  }
}
