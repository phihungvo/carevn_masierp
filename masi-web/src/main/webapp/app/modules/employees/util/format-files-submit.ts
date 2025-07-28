import { IProfileAttachment } from 'app/shared/model/employee.model';
import { PROFILE_ATTACHMENT_TYPE } from 'app/shared/model/enumerations/employee.model';
import { IFIle } from 'app/shared/model/file.model';

export const formatFilesSubmit = (
  submitValues: IProfileAttachment[],
  file: IFIle,
  type: PROFILE_ATTACHMENT_TYPE,
  employeeProfileId: string,
) => {
  if (file) {
    submitValues.push({
      employeeProfileId: employeeProfileId,
      type: type,
      path: file?.id,
    });
  }
};
