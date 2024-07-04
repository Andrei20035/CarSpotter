import 'package:flutter/material.dart';

class UsernameField extends StatefulWidget {
  const UsernameField(
      {super.key,
      required this.controller,
      this.cursorColor = const Color(0xFF434343),
      this.titleColor = const Color(0xFF434343),
      this.keyboardType = TextInputType.text});

  final Color cursorColor;
  final Color titleColor;
  final TextInputType keyboardType;
  final TextEditingController? controller;

  @override
  State<UsernameField> createState() => _UsernameFieldState();
}

class _UsernameFieldState extends State<UsernameField> {
  @override
  void dispose() {
    widget.controller?.dispose();
    super.dispose();
  }

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
            "Username",
            style: Theme.of(context).textTheme.titleLarge!.copyWith(
                  fontSize: 13,
                  color: widget.titleColor,
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
              controller: widget.controller,
              style: Theme.of(context).textTheme.bodyLarge!.copyWith(
                    color: widget.cursorColor,
                    fontSize: 13,
                    fontWeight: FontWeight.w600,
                  ),
              cursorColor: widget.cursorColor,
              keyboardType: widget.keyboardType,
              autocorrect: false,
              textCapitalization: TextCapitalization.none,
              decoration: InputDecoration(
                fillColor: widget.cursorColor,
                border: InputBorder.none,
              ),
              onSaved: (value) {},
            ),
          ),
        ),
      ],
    );
  }
}
