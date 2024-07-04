import 'package:car_spotter/models/preload_app.dart';
import 'package:car_spotter/test_data.dart';
import 'package:car_spotter/ui/screens/app_presentation.dart';
import 'package:car_spotter/models/list_of_screens.dart';
import 'package:car_spotter/ui/screens/feed.dart';
import 'package:car_spotter/ui/screens/friends.dart';

import 'package:car_spotter/ui/screens/login.dart';
import 'package:car_spotter/ui/screens/profile_customization.dart';
import 'package:car_spotter/ui/screens/profile_dashboard.dart';
import 'package:car_spotter/ui/screens/settings.dart';
import 'package:car_spotter/ui/screens/your_car.dart';

import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'package:firebase_core/firebase_core.dart';
import 'firebase_options.dart';

const List<String> assets = [
  "assets/images/icons/google-logo.png",
  "assets/app_presentation/app_presentation_1.png",
  "assets/app_presentation/app_presentation_2.png",
  "assets/app_presentation/app_presentation_3.png",
  "assets/app_presentation/app_presentation_4.png",
  "assets/images/icons/magnifying_glass.png",
  "assets/images/icons/AI_cars.png",
  "assets/images/icons/podium.png",
  "assets/images/placeholder.png",
  "assets/images/profile_picture.jpg",
  "assets/images/icons/points.png",
  "assets/images/dummyCar.png",
  "assets/images/icons/clock.png",
  "assets/images/icons/car.png",
  "assets/images/icons/gps.png",
  "assets/images/profile_picture1.jpg",
  "assets/images/profile_picture2.jpg",
  "assets/images/posts/image1.jpg",
  "assets/images/posts/image2.jpg",
  "assets/images/posts/image3.jpg",
  'assets/images/posts/image4.jpg',
  'assets/images/posts/image5.jpg',
  'assets/images/posts/image6.jpg',
  'assets/images/posts/image7.jpg',
  'assets/images/posts/image8.jpg',
  'assets/images/posts/image9.jpg',
  'assets/images/icons/three-dots.png',
  'assets/images/icons/like.png',
  'assets/images/icons/comment.png',
];

const List<String> appPresentationAssets = [
    "assets/images/icons/google-logo.png",
  "assets/app_presentation/app_presentation_1.png",
  "assets/app_presentation/app_presentation_2.png",
  "assets/app_presentation/app_presentation_3.png",
  "assets/app_presentation/app_presentation_4.png",
  "assets/images/icons/magnifying_glass.png",
  "assets/images/icons/AI_cars.png",
  "assets/images/icons/podium.png",
  "assets/images/placeholder.png",
];

const List<String> currentDayPostAssets = [
  "assets/images/profile_picture.jpg",
  "assets/images/icons/points.png",
  "assets/images/dummyCar.png",
  "assets/images/icons/clock.png",
  "assets/images/icons/car.png",
  "assets/images/icons/gps.png",
];

const List<String> feedPostsAssets = [
  "assets/images/profile_picture1.jpg",
  "assets/images/profile_picture2.jpg",
  "assets/images/posts/image1.jpg",
  "assets/images/posts/image2.jpg",
  "assets/images/posts/image3.jpg",
  'assets/images/posts/image4.jpg',
  'assets/images/posts/image5.jpg',
  'assets/images/posts/image6.jpg',
  'assets/images/posts/image7.jpg',
  'assets/images/posts/image8.jpg',
  'assets/images/posts/image9.jpg',
  'assets/images/icons/three-dots.png',
  'assets/images/icons/like.png',
  'assets/images/icons/comment.png',
];

final theme = ThemeData(
  colorScheme: ColorScheme.fromSeed(
    seedColor: const Color(0xFF080C30),
  ),
  textTheme: GoogleFonts.poppinsTextTheme(),
);


void main() {
  loadTestData();
  runApp(
    const ProviderScope(
      child: PreloadApp(),
    ),
  );
}

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: theme,
      initialRoute: '/AppPresentation',
      routes: {
        '/AppPresentation': (context) =>
            AppPresentation(screens: ScreenList().screens),
        '/login': (context) => const LogInScreen(),
        '/profileCustomization': (context) => const ProfileCustomization(),
        '/yourCar': (context) => const YourCar(),
        '/feed': (context) => FeedScreen(user: dummyUser),
        '/profileDashboard': (context) => const ProfileDashboardScreen(),
        '/friends': (context) => const FriendsScreen(),
        '/settings': (context) =>  const SettingsScreen(),
      },
    );
  }
}

Future<void> preloadAssets(BuildContext context, localAssets) async {
  for (String asset in localAssets) {
    await precacheImage(AssetImage(asset), context);
  }
  
}