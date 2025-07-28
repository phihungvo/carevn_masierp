import { z } from 'zod';

import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';

const NAME_MESSAGE: string = 'Vui lòng nhập tên bộ phận';

export const workspaceSchema = z.object({
  name: z.string({ message: NAME_MESSAGE }).refine(value => value.trim() !== '', { message: NAME_MESSAGE }),
  description: z.any().optional(),
  workspaceType: z.nativeEnum(WORKSPACE_TYPE, { message: 'Vui lòng chọn loại' }),
});

export type WorkspaceFormSchema = z.infer<typeof workspaceSchema>;
