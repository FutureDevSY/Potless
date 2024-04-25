class SearchResultItem {
  final String addressName,
      region_1depthName,
      region_2depthName,
      region_3depthName,
      roadName,
      undergroundYn,
      mainBuildingNo,
      subBuildingNo,
      buildingName,
      zoneNo,
      mountainYn,
      mainAddressNo,
      subAddressNo;

  SearchResultItem({
    required this.addressName,
    required this.region_1depthName,
    required this.region_2depthName,
    required this.region_3depthName,
    required this.roadName,
    required this.undergroundYn,
    required this.mainBuildingNo,
    required this.subBuildingNo,
    required this.buildingName,
    required this.zoneNo,
    required this.mountainYn,
    required this.mainAddressNo,
    required this.subAddressNo,
  });

  factory SearchResultItem.fromJson(Map<String, dynamic> json) {
    return SearchResultItem(
      addressName: json['address_name'],
      region_1depthName: json['region_1depth_name'],
      region_2depthName: json['region_2depth_name'],
      region_3depthName: json['region_3depth_name'],
      roadName: json['road_name'],
      undergroundYn: json['underground_yn'],
      mainBuildingNo: json['main_building_no'],
      subBuildingNo: json['sub_building_no'],
      buildingName: json['building_name'],
      zoneNo: json['zone_no'],
      mountainYn: json['mountain_yn'],
      mainAddressNo: json['main_address_no'],
      subAddressNo: json['sub_address_no'],
    );
  }
}
