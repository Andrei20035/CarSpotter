import 'package:car_spotter/main.dart';
import 'package:car_spotter/models/user.dart';
import 'package:car_spotter/providers/user_provider.dart';
import 'package:car_spotter/ui/widgets/leaderboard.dart';
import 'package:car_spotter/ui/widgets/spot_score.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:simple_gradient_text/simple_gradient_text.dart';

class ProfileDashboardScreen extends ConsumerStatefulWidget {
  const ProfileDashboardScreen({super.key});

  @override
  ConsumerState<ProfileDashboardScreen> createState() =>
      _ProfileDashboardScreenState();
}

class _ProfileDashboardScreenState
    extends ConsumerState<ProfileDashboardScreen> {
  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;
    final screenHeight = MediaQuery.of(context).size.height;
    final gradientHeight = screenHeight * 0.5;
    final User user = ref.watch(userNotifierProvider);

    return Scaffold(
      extendBodyBehindAppBar: true,
      appBar: PreferredSize(
        preferredSize: Size.fromHeight(screenHeight * 0.1),
        child: Container(
          decoration: const BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.topCenter,
              end: Alignment.bottomCenter,
              colors: [
                Color.fromARGB(255, 0, 0, 0),
                Color.fromARGB(200, 0, 0, 0),
                Color.fromARGB(70, 0, 0, 0),
                Color.fromARGB(0, 0, 0, 0),
              ],
              stops: [
                0.5,
                0.7,
                0.9,
                1,
              ],
            ),
          ),
          child: AppBar(
            scrolledUnderElevation: 0.0,
            toolbarHeight: screenHeight * 0.1,
            systemOverlayStyle: SystemUiOverlayStyle.light,
            backgroundColor: Colors.transparent,
            leading: BackButton(
              color: Colors.white,
              onPressed: () {
                Navigator.of(context).pop();
              },
              style: const ButtonStyle(iconSize: WidgetStatePropertyAll(30)),
            ),
            title: Text(
              user.username,
              style: theme.textTheme.bodyMedium!.copyWith(
                fontSize: 17,
                fontWeight: FontWeight.w600,
                color: Colors.white,
                letterSpacing: 1,
              ),
            ),
            actions: [
              IconButton(
                onPressed: () {
                  Navigator.of(context).pushNamed('/settings');
                },
                icon: const Icon(
                  Icons.settings,
                  color: Colors.white,
                ),
              ),
            ],
          ),
        ),
      ),
      body: SingleChildScrollView(
        child: Stack(
          children: [
            Container(
              height: gradientHeight,
              decoration: const BoxDecoration(
                gradient: LinearGradient(
                  colors: [
                    Color(0xFF000000),
                    Color.fromARGB(255, 5, 8, 29),
                  ],
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter,
                ),
              ),
            ),
            Positioned(
              top: gradientHeight - 1,
              bottom: 0,
              left: 0,
              right: 0,
              child: Container(
                color: const Color.fromARGB(255, 5, 8, 29),
              ),
            ),
            Align(
              alignment: Alignment.topCenter,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Container(
                    height: screenHeight * 0.3,
                    width: double.infinity,
                    decoration: BoxDecoration(
                      borderRadius: const BorderRadius.only(
                        bottomLeft: Radius.circular(20),
                        bottomRight: Radius.circular(20),
                      ),
                      image: DecorationImage(
                        image: user.car != null
                            ? user.car!.picture!
                            : const AssetImage(
                                "assets/images/user_car_placeholder.png"),
                        fit: BoxFit.cover,
                      ),
                    ),
                  ),
                  SizedBox(height: screenHeight * 0.1125),
                  Padding(
                    padding:
                        EdgeInsets.symmetric(horizontal: screenWidth * 0.04),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          user.firstName,
                          style: theme.textTheme.bodyMedium!.copyWith(
                            fontSize: 28,
                            fontWeight: FontWeight.w600,
                            color: Colors.white,
                          ),
                        ),
                        Row(
                          children: [
                            Text(
                              user.country,
                              style: theme.textTheme.bodyMedium!.copyWith(
                                fontSize: 20,
                                fontWeight: FontWeight.w500,
                                color: Colors.white,
                              ),
                            ),
                            const Icon(
                              Icons.location_on_outlined,
                              color: Colors.white,
                            )
                          ],
                        ),
                        GradientText(
                          user.calculateRank(),
                          colors: const [
                            Color(0xFFA470BE),
                            Color(0xFFD96570),
                          ],
                          stops: const [
                            0,
                            0.5,
                          ],
                          style: theme.textTheme.bodyMedium!.copyWith(
                            fontSize: 18,
                            color: Colors.white,
                          ),
                        ),
                        SizedBox(height: screenHeight * 0.04125),
                        SpotScore(user: user),
                        SizedBox(height: screenHeight * 0.0325),
                        const Leaderboard(),
                        SizedBox(height: screenHeight * 0.0325),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            Positioned(
              top: screenHeight * 0.225,
              left: 10,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  CircleAvatar(
                    backgroundImage: user.profilePicture,
                    radius: 70,
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
