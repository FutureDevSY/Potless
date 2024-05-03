import 'package:amplify_auth_cognito/amplify_auth_cognito.dart';
import 'package:amplify_flutter/amplify_flutter.dart';
import 'package:amplify_storage_s3/amplify_storage_s3.dart';
// import 'amplifyconfiguration.dart';

void configureAmplify() async {
  AmplifyAuthCognito authPlugin = AmplifyAuthCognito();
  AmplifyStorageS3 storagePlugin = AmplifyStorageS3();

  Amplify.addPlugins([authPlugin, storagePlugin]);

  try {
    // await Amplify.configure(amplifyconfig);
    print("Successfully configured Amplify :tada:");
  } catch (e) {
    print("Could not configure Amplify: $e");
  }
}
