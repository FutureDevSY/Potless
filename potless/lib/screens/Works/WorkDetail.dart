import 'dart:io';

import 'package:camera/camera.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:kakao_map_plugin/kakao_map_plugin.dart';
import 'package:porthole24/widgets/UI/AppBar.dart';
import 'package:porthole24/widgets/UI/ScreenSize.dart';

class WorkDetailScreen extends StatefulWidget {
  final double placeLatitude, placeLongitude, length;
  final int potholeId, status, severity;
  final String address, roadName, field;
  final Image? image;

  const WorkDetailScreen({
    super.key,
    this.placeLatitude = 36.3504567,
    this.placeLongitude = 127.3848187,
    this.potholeId = 0,
    this.status = 1,
    this.severity = 1,
    this.length = 0,
    this.address = '예제',
    this.roadName = '예제',
    this.field = '예제',
    this.image,
  });

  @override
  State<WorkDetailScreen> createState() => _WorkDetailScreenState();
}

class _WorkDetailScreenState extends State<WorkDetailScreen> {
  List<dynamic> imageFiles = [
    'assets/pothole/porthole1.jpg',
  ];
  Set<Marker> markers = {};

  void _onMapCreated(KakaoMapController controller) {
    setState(() {
      controller.setCenter(LatLng(widget.placeLatitude, widget.placeLongitude));
      debugPrint(
        "위도(가로) : ${widget.placeLatitude}, 경도(세로) : ${widget.placeLongitude}",
      );
      markers.add(
        Marker(
          markerId: 'searched_location',
          latLng: LatLng(
            widget.placeLatitude,
            widget.placeLongitude,
          ),
          width: 30,
          height: 44,
          offsetX: 15,
          offsetY: 44,
        ),
      );
    });
  }

  Widget _buildImageBox(dynamic imageSource, String heading, int index) {
    Widget imageWidget;
    if (imageSource is String) {
      imageWidget = Image.asset(imageSource, fit: BoxFit.cover);
    } else if (imageSource is File) {
      imageWidget = Image.file(imageSource, fit: BoxFit.cover);
    } else {
      imageWidget =
          Container(color: Colors.grey); // Fallback for any other case
    }

    return GestureDetector(
      onTap: () async {
        await _showImageSourceActionSheet(context, index);
      },
      child: Container(
        margin: const EdgeInsets.all(5.0),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(5),
          boxShadow: const [
            BoxShadow(
              color: Colors.black26,
              blurRadius: 4,
              offset: Offset(0, 2),
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Expanded(
                child: ClipRRect(
                    borderRadius:
                        const BorderRadius.vertical(top: Radius.circular(5)),
                    child: imageWidget)),
            Container(
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: Colors.black.withOpacity(0.5),
                borderRadius:
                    const BorderRadius.vertical(bottom: Radius.circular(5)),
              ),
              child: Text(
                heading,
                style: const TextStyle(color: Colors.white, fontSize: 16),
                textAlign: TextAlign.center,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildAddImageButton(int index) {
    return GestureDetector(
      onTap: () => _showImageSourceActionSheet(context, index),
      child: Container(
        width: double.infinity,
        margin: const EdgeInsets.all(5.0),
        decoration: BoxDecoration(
          color: Colors.grey.shade300,
          borderRadius: BorderRadius.circular(5),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(
              Icons.add_a_photo,
              color: Colors.grey,
              size: 50,
            ),
            Padding(
              padding: const EdgeInsets.only(top: 8.0),
              child: Text(
                '사진을 추가해주세요',
                style: TextStyle(
                  color: Colors.grey[600],
                  fontSize: 16,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Future<void> _showImageSourceActionSheet(
      BuildContext context, int index) async {
    final ImagePicker picker = ImagePicker();
    showModalBottomSheet(
      context: context,
      builder: (BuildContext context) {
        return SafeArea(
          child: Container(
            color: Colors.white,
            child: Wrap(
              children: <Widget>[
                ListTile(
                  leading: const Icon(Icons.photo_library),
                  title: Container(
                    height: UIhelper.deviceHeight(context) * 0.1,
                    padding: const EdgeInsets.all(20),
                    child: const Center(
                      child: Text(
                        '갤러리',
                        style: TextStyle(fontSize: 20),
                      ),
                    ),
                  ),
                  onTap: () async {
                    final XFile? image =
                        await picker.pickImage(source: ImageSource.gallery);
                    if (image != null) {
                      setState(() {
                        if (index >= imageFiles.length) {
                          imageFiles.add(File(image.path));
                        } else {
                          imageFiles[index] = File(image.path);
                        }
                      });
                    }
                    Navigator.of(context).pop();
                  },
                ),
                const Divider(), // Insert Divider widget here
                ListTile(
                  leading: const Icon(Icons.photo_camera),
                  title: Container(
                    height: UIhelper.deviceHeight(context) * 0.1,
                    padding: const EdgeInsets.all(10),
                    child: const Center(
                      child: Text(
                        '사진 촬영',
                        style: TextStyle(fontSize: 20),
                      ),
                    ),
                  ),
                  onTap: () async {
                    final XFile? photo =
                        await picker.pickImage(source: ImageSource.camera);
                    if (photo != null) {
                      setState(() {
                        if (index >= imageFiles.length) {
                          imageFiles.add(File(photo.path));
                        } else {
                          imageFiles[index] = File(photo.path);
                        }
                      });
                    }
                    Navigator.of(context).pop();
                  },
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    List<String> headings = ["작업 전", "작업 중", "작업 후"];
    return Scaffold(
      appBar: CustomAppBar(title: '${widget.potholeId}번'),
      body: Container(
        padding: const EdgeInsets.all(20),
        color: Colors.white,
        child: Center(
          child: Column(
            children: [
              Container(
                padding: const EdgeInsets.all(5),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(widget.address),
                    Text(widget.roadName),
                  ],
                ),
              ),
              Container(
                decoration: BoxDecoration(
                  border: Border.all(),
                ),
                child: SizedBox(
                  width: UIhelper.deviceWidth(context) * 0.9,
                  height: UIhelper.deviceHeight(context) * 0.3,
                  child: KakaoMap(
                    onMapCreated: _onMapCreated,
                    markers: markers.toList(),
                    center: LatLng(
                      widget.placeLatitude,
                      widget.placeLongitude,
                    ),
                    currentLevel: 4,
                  ),
                ),
              ),
              SizedBox(
                height: UIhelper.deviceHeight(context) * 0.05,
              ),
              const Text('사진'),
              CarouselSlider(
                options: CarouselOptions(
                    height: UIhelper.deviceHeight(context) * 0.4,
                    enlargeCenterPage: true,
                    enableInfiniteScroll: false),
                items: List.generate(3, (index) {
                  if (index < imageFiles.length) {
                    return _buildImageBox(imageFiles[index],
                        headings[index % headings.length], index);
                  }
                  return _buildAddImageButton(imageFiles.length);
                }),
              ),
            ],
          ),
        ),
      ),
    );
  }
}


              // CarouselSlider.builder(
              //   options: CarouselOptions(
              //     height: UIhelper.deviceHeight(context) * 0.3,
              //     enlargeCenterPage: true,
              //     onPageChanged: (index, reason) {
              //       if (index >= imageFiles.length) {
              //         _showImageSourceActionSheet(context);
              //       }
              //     },
              //   ),
              //   itemCount: imageFiles.length + 1, // +1 for the placeholder for new imageFiles
              //   itemBuilder: (BuildContext context, int itemIndex, int pageViewIndex) {
              //     if (itemIndex < imageFiles.length) {
              //       return Container(
              //         margin: EdgeInsets.all(5.0),
              //         decoration: BoxDecoration(
              //           borderRadius: BorderRadius.circular(5),
              //           image: DecorationImage(
              //             image: imageFiles[itemIndex],
              //             fit: BoxFit.cover,
              //           ),
              //         ),
              //       );
              //     } else {
              //       // This is the 'add new image' box
              //       return GestureDetector(
              //         onTap: () => _showImageSourceActionSheet(context),
              //         child: Container(
              //           margin: EdgeInsets.all(5.0),
              //           decoration: BoxDecoration(
              //             color: Colors.grey.shade300,
              //             borderRadius: BorderRadius.circular(5),
              //           ),
              //           child: const Center(
              //             child: Icon(
              //               Icons.add_a_photo,
              //               color: Colors.grey,
              //               size: 50,
              //             ),
              //           ),
              //         ),
              //       );
              //     }
              //   },
              // ),