/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.test.orchestrator.junit;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import org.junit.runner.Result;

/** Parcelable imitation of a JUnit ParcelableResult */
public final class ParcelableResult implements Parcelable {
  private final List<ParcelableFailure> mFailures;

  public ParcelableResult(Result result) {
    mFailures = new ArrayList<>();
    for (org.junit.runner.notification.Failure failure : result.getFailures()) {
      mFailures.add(new ParcelableFailure(failure));
    }
  }

  private ParcelableResult(Parcel in) {
    mFailures = new ArrayList<>();
    Object[] failures = in.readArray(ParcelableFailure[].class.getClassLoader());
    for (Object failure : failures) {
      mFailures.add((ParcelableFailure) failure);
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeArray(mFailures.toArray());
  }

  public static final Creator<ParcelableResult> CREATOR =
      new Creator<ParcelableResult>() {
        @Override
        public ParcelableResult createFromParcel(Parcel in) {
          return new ParcelableResult(in);
        }

        @Override
        public ParcelableResult[] newArray(int size) {
          return new ParcelableResult[size];
        }
      };

  public boolean wasSuccessful() {
    return mFailures.isEmpty();
  }

  public List<ParcelableFailure> getFailures() {
    return mFailures;
  }

  public int getFailureCount() {
    return mFailures.size();
  }
}
