import 'package:flutter/material.dart';

class EmailField extends StatelessWidget {
  const EmailField({
    super.key,
    required this.controller,
    this.cursorColor = const Color(0xFF434343),
    this.titleColor = const Color(0xFF434343),
    this.keyboardType = TextInputType.text,
  });

  final Color cursorColor;
  final Color titleColor;
  final TextInputType keyboardType;
  final TextEditingController controller;

  @override
  Widget build(BuildContext context) {
    final screenHeight = MediaQuery.of(context).size.height;
    final screenWidth = MediaQuery.of(context).size.width;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: EdgeInsets.only(left: screenWidth * 0.07),
          child: Text(
            "Email address",
            style: Theme.of(context).textTheme.titleLarge!.copyWith(
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
              controller: controller,
              style: Theme.of(context).textTheme.bodyLarge!.copyWith(
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
              validator: (value) {
                if (value == null ||
                    value.trim().isEmpty ||
                    !value.contains('@')) {
                  return "Please enter a valid email address";
                }
                return null;
              },
              onSaved: (value) {
                controller.text = value ?? '';
              },
            ),
          ),
        ),
      ],
    );
  }
}
