class Pothole {
  final int potholeId;
  final int areaId;
  final int severity;
  final double dirX;
  final double dirY;
  final String address;
  final String field;
  final String roadName;
  final int status;
  final double length;

  Pothole({
    required this.potholeId,
    required this.areaId,
    required this.severity,
    required this.dirX,
    required this.dirY,
    required this.address,
    required this.field,
    required this.roadName,
    required this.status,
    required this.length,
  });

  factory Pothole.fromJson(Map<String, dynamic> json) {
    return Pothole(
      potholeId: json['potholeId'],
      areaId: json['areaId'],
      severity: json['severity'],
      dirX: json['dir_x'],
      dirY: json['dir_y'],
      address: json['address'],
      field: json['field'],
      roadName: json['roadName'],
      status: json['status'],
      length: json['length'],
    );
  }
}
