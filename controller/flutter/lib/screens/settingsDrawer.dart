import 'package:flutter/material.dart';

class SettingsDrawer extends StatefulWidget {
  const SettingsDrawer({super.key});

  @override
  _SettingsDrawerState createState() => _SettingsDrawerState();
}

class _SettingsDrawerState extends State<SettingsDrawer> {
  List<bool> isSelected = [false, false];

  @override
  Widget build(BuildContext context) {
    return Drawer(
      backgroundColor: Colors.grey,
      child: ListView(
        padding: EdgeInsets.zero,
        children: [
          const Padding(
            padding: EdgeInsets.all(16.0),
          ),
          Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: Row(children: [
                const Text(
                  'Choose Controller',
                  style: TextStyle(
                    fontSize: 15,
                    fontWeight: FontWeight.w500,
                    color: Color(0xFFffffff),
                  ),
                ),
                const SizedBox(width: 10,),
                ToggleButtons(
                  onPressed: (int newIndex) {
                    setState(() {
                      for (int index = 0; index < isSelected.length; index++) {
                        if (index == newIndex) {
                          isSelected[index] = true;
                        } else {
                          isSelected[index] = false;
                        }
                      }
                    });
                  },
                  isSelected: isSelected,
                  children: [
                    Image.asset(
                      "images/controller_icon.png",
                      height: 18,
                      width: 18,
                    ),
                    Image.asset(
                      "images/tilting_phone_icon.png",
                      height: 18,
                      width: 18,
                    ),
                  ],
                ),
              ])),
          // Add more ListTile widgets for additional buttons
        ],
      ),
    );
  }
}
