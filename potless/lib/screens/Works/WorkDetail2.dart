import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:intl/intl.dart';
import 'package:kakao_map_plugin/kakao_map_plugin.dart';
import 'package:potless/models/pothole.dart';
import 'package:potless/widgets/UI/AppBar.dart';
import 'package:potless/widgets/UI/ScreenSize.dart';
import 'package:potless/widgets/blocks/carousel_block.dart'; // Assuming your model is set up correctly

class WorkDetailScreen extends StatefulWidget {
  final double placeLatitude, placeLongitude, width;
  final int potholeId, severity;
  final String address, roadName, status, field, dType;
  final List<DamageImage> images;
  final DateTime createdAt;

  const WorkDetailScreen({
    super.key,
    this.placeLatitude = 36.3504567,
    this.placeLongitude = 127.3848187,
    this.potholeId = 0,
    this.status = '작업 전',
    this.severity = 1,
    this.width = 0,
    this.address = '예제',
    this.roadName = '예제',
    this.field = '예제',
    required this.images,
    required this.dType,
    required this.createdAt,
  });

  @override
  _WorkDetailScreenState createState() => _WorkDetailScreenState();
}

class _WorkDetailScreenState extends State<WorkDetailScreen> {
  final ImagePicker picker = ImagePicker();
  List<Widget> imageWidgets = [];
  int filledImageSlots = 1; // Start with one always filled

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

  @override
  void initState() {
    super.initState();

    // Ensuring there are always three slots
    while (widget.images.length < 3) {
      widget.images
          .add(DamageImage(id: -1, url: '', order: widget.images.length + 1));
    }

    // Prepare image widgets
    imageWidgets = widget.images
        .map((image) => CustomCarouselCard(
              image: image,
              onTap: () {
                debugPrint("78Image URL: '${image.url}'");

                if (image.url.isEmpty) {
                  debugPrint("81URL is empty, showing action sheet.");
                  _showImageSourceActionSheet(
                      context, widget.images.indexOf(image));
                } else {
                  debugPrint("85URL is not empty, image order: ${image.order}");
                  // Define other actions here if the image is not empty
                  debugPrint("87Tapped image with order: ${image.order}");
                }
              },
            ))
        .toList();
  }

  Future<void> pickImage(int index) async {
    final XFile? image = await picker.pickImage(source: ImageSource.gallery);
    if (image != null) {
      setState(() {
        while (imageWidgets.length <= index) {
          imageWidgets
              .add(Container()); // Ensure the list has an index position
        }
        imageWidgets[index] = Image.file(File(image.path), fit: BoxFit.cover);
      });
    }
  }

  Widget buildImageItem(Widget image, bool isInteractive) {
    return GestureDetector(
      onTap:
          isInteractive ? () => pickImage(imageWidgets.indexOf(image)) : null,
      child: Container(
        width: MediaQuery.of(context).size.width,
        margin: const EdgeInsets.symmetric(horizontal: 5.0),
        child: ClipRRect(
          borderRadius: const BorderRadius.all(Radius.circular(5.0)),
          child: image,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    String severityText() {
      switch (widget.severity) {
        case 1:
          return '양호';
        case 2:
          return '중간';
        case 3:
          return '심각';
        default:
          return '미정';
      }
    }

    return Scaffold(
      appBar: CustomAppBar(title: '파손 번호 - ${widget.potholeId}'),
      body: Container(
        color: const Color(0xffffffff),
        child: SingleChildScrollView(
          child: Center(
            child: Column(
              children: [
                Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text('주소: ${widget.address}'),
                      Text('종류: ${widget.dType}'),
                    ],
                  ),
                ),
                Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text('너비: ${widget.width}mm'),
                      Text(
                          '촬영 일시:  ${DateFormat('yyyy-MM-dd').format(widget.createdAt)}'),
                    ],
                  ),
                ),
                Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text('좌표 x: ${widget.placeLongitude}'),
                      Text('심각도: ${severityText()}'),
                    ],
                  ),
                ),
                Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text('좌표 y: ${widget.placeLatitude}'),
                      Text('작업 상태: ${widget.status}'),
                    ],
                  ),
                ),
                SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
                Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
                  child: const Row(
                    children: [
                      Text('지도'),
                    ],
                  ),
                ),
                Container(
                  decoration: BoxDecoration(
                    border: Border.all(),
                  ),
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
                SizedBox(
                  height: UIhelper.deviceHeight(context) * 0.05,
                ),
                CarouselSlider(
                  items: widget.images
                      .map((image) => CustomCarouselCard(
                            image: image,
                            onTap: () {
                              if (image.url.isEmpty) {
                                _showImageSourceActionSheet(
                                    context, widget.images.indexOf(image));
                              } else {
                                debugPrint('167 tapped ${image.order}');
                              }
                            },
                          ))
                      .toList(),
                  options: CarouselOptions(
                    height: 400,
                    enlargeCenterPage: true,
                    enableInfiniteScroll: false,
                    viewportFraction: 0.8,
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void _showImageSourceActionSheet(BuildContext context, int index) async {
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
                  title: const Text('갤러리', style: TextStyle(fontSize: 20)),
                  onTap: () async {
                    final XFile? image =
                        await picker.pickImage(source: ImageSource.gallery);
                    if (image != null) {
                      setState(() {
                        var newImage = DamageImage(
                            id: -1, url: image.path, order: index + 1);
                        widget.images[index] = newImage;
                        imageWidgets[index] = CustomCarouselCard(
                          image: newImage,
                          onTap: () => debugPrint(
                              "Tapped new image with order: ${newImage.order}"),
                        );
                      });
                      Navigator.of(context).pop();
                    }
                  },
                ),
                const Divider(),
                ListTile(
                  leading: const Icon(Icons.photo_camera),
                  title: const Text('사진 촬영', style: TextStyle(fontSize: 20)),
                  onTap: () async {
                    final XFile? photo =
                        await picker.pickImage(source: ImageSource.camera);
                    if (photo != null) {
                      setState(() {
                        var newImage = DamageImage(
                            id: -1, url: photo.path, order: index + 1);
                        widget.images[index] = newImage;
                        imageWidgets[index] = CustomCarouselCard(
                          image: newImage,
                          onTap: () => debugPrint(
                              "Tapped new image with order: ${newImage.order}"),
                        );
                      });
                      Navigator.of(context).pop();
                    }
                  },
                ),
              ],
            ),
          ),
        );
      },
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

  Widget _buildImageBox(dynamic imageSource, String heading, int index) {
    Widget imageWidget;
    if (imageSource is String) {
      imageWidget = Image.asset(imageSource, fit: BoxFit.cover);
    } else if (imageSource is File) {
      imageWidget = Image.file(imageSource, fit: BoxFit.cover);
    } else {
      imageWidget = Container(color: Colors.grey);
    }

    return GestureDetector(
      onTap: () async {
        _showImageSourceActionSheet(context, index);
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
}
