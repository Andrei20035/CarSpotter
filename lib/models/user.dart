import 'package:car_spotter/models/post.dart';
import 'package:car_spotter/models/user_car.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:uuid/uuid.dart';

class User {
  String id;
  ImageProvider? profilePicture;
  String firstName;
  String lastName;
  String username;
  String country;
  int spotScore;
  Car? car;
  final List<Post> posts;
  final List<Post> currentDayPosts;
  final List<User> friends;
  final List<User> friendRequests;
  final Map<String, List<int>> ranks = {
    "Street Cruiser": [0, 499],
    "Road Warrior": [500, 999],
    "Highway Explorer": [1000, 1499],
    "Auto Enthusiast": [1500, 1999],
    "Master Collector": [2000]
  };

  User({
    String? id,
    this.profilePicture,
    required this.username,
    required this.firstName,
    required this.lastName,
    required this.country,
    this.spotScore = 0,
    this.car,
    List<Post>? currentDayPosts,
    List<Post>? posts,
    List<User>? friends,
    List<User>? friendRequests,
  })  : id = id ?? const Uuid().v4(),
        posts = posts ?? [],
        currentDayPosts = currentDayPosts ?? [],
        friends = friends ?? [],
        friendRequests = friendRequests ?? [];

  User copyWith({
    String? id,
    ImageProvider? profilePicture,
    String? firstName,
    String? lastName,
    String? username,
    String? country,
    int? spotScore,
    Car? car,
    final List<Post>? posts,
    final List<Post>? currentDayPosts,
    final List<User>? friends,
    final List<User>? friendRequests,
  }) {
    return User(
      id: id ?? this.id,
      profilePicture: profilePicture ?? this.profilePicture,
      firstName: firstName ?? this.firstName,
      lastName: lastName ?? this.lastName,
      username: username ?? this.username,
      country: country ?? this.country,
      spotScore: spotScore ?? this.spotScore,
      car: car ?? this.car,
      posts: posts ?? this.posts,
      currentDayPosts: currentDayPosts ?? this.currentDayPosts,
      friends: friends ?? this.friends,
      friendRequests: friendRequests ?? this.friendRequests,
    );
  }

  void updateProfilePicture(ImageProvider newProfilePicture) {
    profilePicture = newProfilePicture;
  }

  ImageProvider? getProfilePicture() {
    return profilePicture;
  }

  void updateFirstName(String newFirstName) {
    firstName = newFirstName;
  }

  void updateLastName(String newLastName) {
    lastName = newLastName;
  }

  void updateUsername(String newUsername) {
    username = newUsername;
  }

  void updateCountry(String newCountry) {
    country = newCountry;
  }

  void updateCar(Car newCar) {
    car = newCar;
  }

  void addPost(Post newPost) {
    posts.add(newPost);
    currentDayPosts.add(newPost);
  }

  void deletePost(String id) {
    posts.removeWhere((post) => post.id == id);
    currentDayPosts.removeWhere((post) => post.id == id);
  }

  void resetTodayPosts() {
    currentDayPosts.clear();
  }

  void addFriend(User newUser) {
    if(!friends.any((user) => user.id == newUser.id)) {
      friends.add(newUser);
    }
  }

  void removeFriend(String id) {
    friends.removeWhere((user) => user.id == id);
  }

  String calculateRank() {
    if (spotScore >= 0 && spotScore < 500) {
      return "Street Cruiser";
    } else if (spotScore >= 500 && spotScore < 1000) {
      return "Road Warrior";
    } else if (spotScore >= 1000 && spotScore < 1500) {
      return "Highway Explorer";
    } else if (spotScore >= 1500 && spotScore < 2000) {
      return "Auto Enthusiast";
    } else {
      return "Master Collector";
    }
  }

  int calculateRemainingPoints() {
    final String rank = calculateRank();
    return ranks[rank]![1] - spotScore + 1;
  }

  double calculateProgress() {
    final String rank = calculateRank();
    final int range = ranks[rank]![1] - ranks[rank]![0];
    final int progress = spotScore - ranks[rank]![0];
    print(progress / range);
    return progress / range;
  }

  List<int>? calculateRange() {
    return ranks[calculateRank()];
  }
}
