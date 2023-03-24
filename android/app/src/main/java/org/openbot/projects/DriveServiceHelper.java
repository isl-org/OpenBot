package org.openbot.projects;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.services.drive.Drive;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {

    private final Drive mDriveService;
    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }

    public Task<String> readFile(String fileId) {
        return Tasks.call(mExecutor, () -> {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            mDriveService.files().get(fileId)
                    .executeMediaAndDownloadTo(outputStream);
            return outputStream.toString();
        });
    }
}

