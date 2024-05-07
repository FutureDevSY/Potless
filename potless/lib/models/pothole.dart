class DamageImage {
  final int id;
  final String url;
  final int order;

  DamageImage({
    required this.id,
    required this.url,
    required this.order,
  });

  factory DamageImage.fromJson(Map<String, dynamic> json) {
    return DamageImage(
      id: json['id'],
      url: json['url'],
      order: json['order'],
    );
  }
}

class DamageResponse {
  final int id, severity;
  final double dirX, dirY, width;
  final String address, status, area, location, dtype;
  final DateTime createdDateTime;
  final List<DamageImage> images;

  DamageResponse({
    required this.width,
    required this.id,
    required this.severity,
    required this.dirX,
    required this.dirY,
    required this.address,
    required this.status,
    required this.area,
    required this.location,
    required this.dtype,
    required this.createdDateTime,
    required this.images,
  });

  factory DamageResponse.fromJson(Map<String, dynamic> json) {
    var imagesList = json['imagesResponseDTOS'] as List;
    List<DamageImage> images =
        imagesList.map((i) => DamageImage.fromJson(i)).toList();

    return DamageResponse(
      id: json['id'],
      severity: json['severity'],
      dirX: json['dirX'].toDouble(),
      dirY: json['dirY'].toDouble(),
      address: json['address'],
      width: json['width'],
      status: json['status'],
      area: json['area'],
      location: json['location'],
      dtype: json['dtype'],
      createdDateTime: DateTime.parse(json['createdDateTime']),
      images: images,
    );
  }
}

class Project {
  final int teamId;
  final int projectId;
  final String projectName;
  final DateTime projectDate;
  final int projectSize;
  final DateTime createdDate;
  final List<DamageResponse> damages;

  Project({
    required this.teamId,
    required this.projectId,
    required this.projectName,
    required this.projectDate,
    required this.projectSize,
    required this.createdDate,
    required this.damages,
  });

  factory Project.fromJson(Map<String, dynamic> json) {
    var damagesList = json['damangeResponseDtoList'] as List;
    List<DamageResponse> damages =
        damagesList.map((d) => DamageResponse.fromJson(d)).toList();

    return Project(
      teamId: json['teamId'],
      projectId: json['projectId'],
      projectName: json['projectName'],
      projectDate: DateTime.parse(json['projectDate']),
      projectSize: json['projectSize'],
      createdDate: DateTime.parse(json['createdDate']),
      damages: damages,
    );
  }
}
