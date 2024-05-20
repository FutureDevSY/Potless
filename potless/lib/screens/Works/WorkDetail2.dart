import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:intl/intl.dart';
import 'package:kakao_map_plugin/kakao_map_plugin.dart';
import 'package:potless/API/api_request.dart';
import 'package:potless/models/pothole.dart';
import 'package:potless/widgets/UI/AppBar.dart';
import 'package:potless/widgets/UI/ScreenSize.dart';
import 'package:potless/widgets/blocks/carousel_block.dart';
import 'package:potless/widgets/buttons/714_100button.dart';

class WorkDetailScreen extends StatefulWidget {
  final double placeLatitude, placeLongitude;
  final int potholeId, severity, width;
  final String address, roadName, status, field, dType;
  final List<DamageImage> images;
  final DateTime createdAt;
  final VoidCallback onProjectUpdate;
  final VoidCallback onTaskUpdate;

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
    required this.onProjectUpdate,
    required this.onTaskUpdate,
  });

  @override
  _WorkDetailScreenState createState() => _WorkDetailScreenState();
}

class _WorkDetailScreenState extends State<WorkDetailScreen> {
  final ApiService _apiService = ApiService();
  final ImagePicker picker = ImagePicker();
  List<Widget> imageWidgets = [];
  int filledImageSlots = 1;

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

    while (widget.images.length < 3) {
      widget.images
          .add(DamageImage(id: -1, url: '', order: widget.images.length + 1));
    }

    imageWidgets = widget.images
        .map((image) => CustomCarouselCard(
              image: image,
              onTap: () {
                if (image.url.isEmpty) {
                  debugPrint("81URL is empty, showing action sheet.");
                  _showImageSourceActionSheet(
                      context, widget.images.indexOf(image));
                } else {
                  debugPrint("85URL is not empty, image order: ${image.order}");
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
          imageWidgets.add(Container());
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

    String typeText() {
      switch (widget.dType) {
        case 'POTHOLE':
          return '포트홀';
        default:
          return '도로파손';
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
                      Text(
                        '주소: ${widget.address}',
                        style: const TextStyle(fontSize: 16),
                      ),
                      Text(
                        '종류: ${typeText()}',
                        style: const TextStyle(fontSize: 16),
                      ),
                    ],
                  ),
                ),
                Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        '너비: ${widget.width}mm',
                        style: const TextStyle(fontSize: 16),
                      ),
                      Text(
                        '심각도: ${severityText()}',
                        style: const TextStyle(fontSize: 16),
                      ),
                    ],
                  ),
                ),
                Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        '촬영 일시:  ${DateFormat('yyyy-MM-dd').format(widget.createdAt)}',
                        style: const TextStyle(fontSize: 16),
                      ),
                    ],
                  ),
                ),
                Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        '작업 상태: ${widget.status}',
                        style: const TextStyle(fontSize: 16),
                      ),
                    ],
                  ),
                ),
                SizedBox(height: UIhelper.deviceHeight(context) * 0.02),
                Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
                  child: const Row(
                    children: [
                      Text(
                        '지도',
                        style: TextStyle(color: Colors.black54),
                      ),
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
                              if (image.url.startsWith('http') ||
                                  image.url.startsWith('https')) {
                                debugPrint('167 tapped ${image.order}');
                              } else {
                                _showImageSourceActionSheet(
                                    context, widget.images.indexOf(image));
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
                SizedBox(height: UIhelper.deviceHeight(context) * 0.05),
                if (widget.status != '작업완료') ...{
                  button714_100(
                    lable: '작업 완료',
                    onPressed: () {
                      if (_areAllImagesSet()) {
                        _showConfirmationDialog(
                          context,
                          widget.images,
                          widget.potholeId.toString(),
                        );
                      } else {
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(
                            content: Text("입력되지 않은 사진이 있습니다"),
                            duration: Duration(seconds: 2),
                          ),
                        );
                      }
                    },
                  ),
                } else ...{
                  const button714_100(
                    lable: '완료 됨',
                  )
                },
                SizedBox(height: UIhelper.deviceHeight(context) * 0.05),
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

  void _showConfirmationDialog(
      BuildContext context, List<DamageImage> images, String potholeId) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext dialogContext) {
        return AlertDialog(
          surfaceTintColor: const Color(0xffFFFFFF),
          backgroundColor: const Color(0xffFFFFFF),
          title: const Text('작업 완료'),
          content: const Text('작업 완료 하시겠습니까?'),
          actions: <Widget>[
            TextButton(
              child: const Text('취소'),
              onPressed: () {
                Navigator.of(dialogContext).pop();
              },
            ),
            TextButton(
              child: const Text('완료'),
              onPressed: () {
                Navigator.of(dialogContext).pop();
                _showLoadingIndicator(context);
                _startUpload(context, images, potholeId);
              },
            ),
          ],
        );
      },
    );
  }

  void _showLoadingIndicator(BuildContext context) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return const Center(
          child: CircularProgressIndicator(),
        );
      },
    );
  }

  Future<void> _startUpload(
      BuildContext context, List<DamageImage> images, String potholeId) async {
    try {
      await handleUploadSequence(images, potholeId);

      widget.onProjectUpdate();
      widget.onTaskUpdate();

      Navigator.of(context).pop();
    } catch (error) {
      Navigator.of(context).pop();
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: const Text('Error'),
            content: const Text(
                'An error occurred during upload. Please try again.'),
            actions: <Widget>[
              TextButton(
                child: const Text('OK'),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              ),
            ],
          );
        },
      );
    }
  }

  // void _showConfirmationDialog(
  //     BuildContext context, List<DamageImage> images, String potholeId) {
  //   showDialog(
  //     context: context,
  //     barrierDismissible: false,
  //     builder: (BuildContext dialogContext) {
  //       return StatefulBuilder(
  //         builder: (BuildContext context, StateSetter setState) {
  //           bool isLoading = false;

  //           void startUpload() async {
  //             setState(() {
  //               isLoading = true;
  //             });

  //             try {
  //               await handleUploadSequence(images, potholeId);

  //               widget.onProjectUpdate();

  //               widget.onTaskUpdate();

  //               Navigator.of(dialogContext).pop();
  //               Navigator.of(dialogContext).pop();
  //             } catch (error) {
  //               // Handle error here, possibly showing an error message
  //               setState(() {
  //                 isLoading = false;
  //               });
  //             }
  //           }

  //           return Stack(
  //             children: [

  //               AlertDialog(
  //                 surfaceTintColor: const Color(0xffFFFFFF),
  //                 backgroundColor: const Color(0xffFFFFFF),
  //                 title: const Text('작업 완료'),
  //                 content: const Text('작업 완료 하시겠습니까?'),
  //                 actions: <Widget>[
  //                   if (!isLoading)
  //                     TextButton(
  //                       child: const Text('취소'),
  //                       onPressed: () {
  //                         Navigator.of(dialogContext).pop();
  //                       },
  //                     ),
  //                   if (!isLoading)
  //                     TextButton(
  //                       child: const Text('완료'),
  //                       onPressed: () {
  //                         startUpload();
  //                       },
  //                     ),
  //                 ],
  //               ),

  //               if (isLoading)
  //                 Positioned.fill(
  //                   child: Container(
  //                     color: Colors.black45,
  //                     child: const Center(
  //                       child: CircularProgressIndicator(
  //                         value: 50,
  //                       ),
  //                     ),
  //                   ),
  //                 ),
  //             ],
  //           );
  //         },
  //       );
  //     },
  //   );
  // }

  Future<void> handleUploadSequence(
    List<DamageImage> images,
    String potholeId,
  ) async {
    if (images.length < 3) {
      debugPrint("Insufficient images provided.");
      return;
    }

    if (images[1].url.startsWith('http') || images[1].url.startsWith('https')) {
      debugPrint('WorkDetail2 377: no need');
    } else {
      bool uploadDuringSuccess =
          await _apiService.damageDuring(potholeId, File(images[1].url));
      if (!uploadDuringSuccess) {
        debugPrint("Failed to upload 'during' image.");
        return;
      }
    }

    if (images[1].url.startsWith('http') || images[1].url.startsWith('https')) {
      debugPrint('WorkDetail2 388: no need');
    } else {
      bool uploadAfterSuccess =
          await _apiService.damageAfter(potholeId, File(images[2].url));
      if (!uploadAfterSuccess) {
        debugPrint("Failed to upload 'after' image.");
        return;
      }
    }
    debugPrint('393: workdone start');
    await _apiService.workDone(widget.potholeId);
    debugPrint('395: workdone end');
  }

  bool _areAllImagesSet() {
    return widget.images.every((image) => image.url.isNotEmpty);
  }
}
