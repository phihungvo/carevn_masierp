export interface ICompany {
  id?: string;
  name?: string;
  description?: string | null;
  parentId?: string | null;
}

export const defaultValue: Readonly<ICompany> = {};
