import 'package:car_spotter/main.dart';
import 'package:car_spotter/ui/screens/splash_screen.dart';
import 'package:car_spotter/ui/widgets/login_button.dart';
import 'package:car_spotter/ui/widgets/login_screen/email.dart';
import 'package:car_spotter/ui/widgets/login_screen/password.dart';
import 'package:car_spotter/ui/widgets/login_screen/username.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:simple_gradient_text/simple_gradient_text.dart';

final _firebase = FirebaseAuth.instance;

class LogInScreen extends StatefulWidget {
  const LogInScreen({super.key});

  @override
  State<LogInScreen> createState() => _LogInScreenState();
}

class _LogInScreenState extends State<LogInScreen> {
  final double containerHeightLogIn = 0.5715;
  final double containerHeightSignUp = 0.6725;
  final double titleSpaceSignUp = 0.0723;
  final double titleSpaceLogIn = 0.123;

  double containerHeight = 0.571;
  double titleSpace = 0.123;

  final _form = GlobalKey<FormState>();

  late TextEditingController? usernameController;
  late TextEditingController emailController;
  late TextEditingController passwordController;

  String _enteredUsername = '';
  String _enteredEmail = '';
  String _enteredPassword = '';

  bool isSignUp = false;
  bool isLoading = false;

  void _submit() async {
    setState(() {
      isLoading = true;
    });

    final isValid = _form.currentState!.validate();

    if (!isValid) {
      setState(() {
        isLoading = false;
      });
      return;
    }

    _form.currentState!.save();
    _enteredEmail = emailController.text;
    _enteredPassword = passwordController.text;
    _enteredUsername = usernameController!.text;

    try {
      if (isSignUp) {
        // ignore: unused_local_variable
        final userCredentials = await _firebase.createUserWithEmailAndPassword(
            email: _enteredEmail, password: _enteredPassword);
      } else {
        // ignore: unused_local_variable
        final userCredentials = await _firebase.signInWithEmailAndPassword(
            email: _enteredEmail, password: _enteredPassword);
      }
      if (mounted) {
        Navigator.pushNamedAndRemoveUntil(
          context,
          '/profileCustomization',
          ModalRoute.withName('/'),
        );
      }
    } on FirebaseAuthException catch (error) {
      if (mounted) {
        ScaffoldMessenger.of(context).clearSnackBars();
        ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(error.message ?? 'Authentication failed')));
      }
    } finally {
      setState(() {
        isLoading = false;
      });
    }
  }

  Future<void> _googleSignIn() async {
    setState(() {
      isLoading = true;
    });

    final GoogleSignIn googleSignIn = GoogleSignIn();
    final GoogleSignInAccount? googleUser = await googleSignIn.signIn();

    if (googleUser != null) {
      final GoogleSignInAuthentication googleAuth =
          await googleUser.authentication;

      final AuthCredential credential = GoogleAuthProvider.credential(
        accessToken: googleAuth.accessToken,
        idToken: googleAuth.idToken,
      );

      try {
        // ignore: unused_local_variable
        final UserCredential userCredential =
            await _firebase.signInWithCredential(credential);
        if (mounted) {
          Navigator.pushNamedAndRemoveUntil(
            context,
            '/profileCustomization',
            ModalRoute.withName('/'),
          );
        }
      } on FirebaseAuthException catch (e) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(e.message ?? 'Google Sign-In failed'),
            ),
          );
        }
      } finally {
        setState(() {
          isLoading = false;
        });
      }
    } else {
      setState(() {
        isLoading = false;
      });
    }
  }

  @override
  void initState() {
    super.initState();
    usernameController = TextEditingController();
    emailController = TextEditingController();
    passwordController = TextEditingController();
  }

  @override
  void dispose() {
    usernameController?.dispose();
    emailController.dispose();
    passwordController.dispose();
    super.dispose();
  }

  void signUp() {
    setState(() {
      isSignUp = true;
      containerHeight = containerHeightSignUp;
      titleSpace = titleSpaceSignUp;
      usernameController ??= TextEditingController();
    });
  }

  void logIn() {
    setState(() {
      isSignUp = false;
      containerHeight = containerHeightLogIn;
      titleSpace = titleSpaceLogIn;
      usernameController?.dispose();
      usernameController = null;
    });
  }

  @override
  Widget build(BuildContext context) {
    final screenHeight = MediaQuery.of(context).size.height;
    final screenWidth = MediaQuery.of(context).size.width;

    return isLoading
        ? const SplashScreen()
        : Scaffold(
            resizeToAvoidBottomInset: true,
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
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        SizedBox(height: screenHeight * 0.055),
                        Padding(
                          padding: EdgeInsets.symmetric(
                            vertical: screenHeight * titleSpace,
                          ),
                          child: Column(
                            children: [
                              GradientText(
                                "CarSpotter",
                                colors: const [
                                  Color(0xFF4285F4),
                                  Color(0xFF9B72CB),
                                  Color(0xFFD96570),
                                  Color(0xFFA470A8),
                                ],
                                stops: const [
                                  0.03,
                                  0.31,
                                  0.84,
                                  1,
                                ],
                                style: theme.textTheme.titleLarge!.copyWith(
                                  fontSize: 55,
                                  fontWeight: FontWeight.w600,
                                  letterSpacing: 0,
                                ),
                              ),
                              Text(
                                "Spot. Snap. Share.",
                                style: theme.textTheme.titleLarge!.copyWith(
                                    fontSize: 16,
                                    fontWeight: FontWeight.w600,
                                    color: Colors.white),
                              ),
                            ],
                          ),
                        ),
                        Container(
                          height: screenHeight * containerHeight,
                          width: double.infinity,
                          decoration: const ShapeDecoration(
                            color: Color(0xFFF0F0F0),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.only(
                                topLeft: Radius.circular(30),
                                topRight: Radius.circular(30),
                              ),
                            ),
                          ),
                          child: Form(
                            key: _form,
                            child: Padding(
                              padding: EdgeInsets.symmetric(
                                  horizontal: screenWidth * 0.06),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  SizedBox(height: screenHeight * 0.05),
                                  if (isSignUp)
                                    Column(
                                      children: [
                                        SizedBox(height: screenHeight * 0.0032),
                                        UsernameField(
                                            controller: usernameController),
                                        SizedBox(height: screenHeight * 0.015),
                                      ],
                                    ),
                                  EmailField(controller: emailController),
                                  SizedBox(height: screenHeight * 0.015),
                                  PasswordField(controller: passwordController),
                                  SizedBox(height: screenHeight * 0.095),
                                  LoginButton(
                                    text: isSignUp ? "Sign Up" : "Log In",
                                    color: const Color(0xFFF0AB25),
                                    onPressed: _submit,
                                  ),
                                  SizedBox(height: screenHeight * 0.03),
                                  LoginButton(
                                    text: isSignUp
                                        ? "Sign Up with Google"
                                        : "Log In with Google",
                                    color: const Color(0xFFD9D9D9),
                                    onPressed: _googleSignIn,
                                    icon: true,
                                  ),
                                  SizedBox(height: screenHeight * 0.025),
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      Text(
                                        isSignUp
                                            ? "Already have an account?"
                                            : "Don't have an acount?",
                                        style:
                                            theme.textTheme.bodyLarge!.copyWith(
                                          fontSize: 13,
                                          color: Colors.black,
                                          fontWeight: FontWeight.w600,
                                        ),
                                      ),
                                      InkWell(
                                        onTap: () {
                                          isSignUp ? logIn() : signUp();
                                        },
                                        child: Text(
                                          isSignUp ? " Log In" : " Sign Up",
                                          style: theme.textTheme.bodyLarge!
                                              .copyWith(
                                            fontSize: 13,
                                            color: const Color(0xFFF0AB25),
                                            fontWeight: FontWeight.w600,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
          );
  }
}
