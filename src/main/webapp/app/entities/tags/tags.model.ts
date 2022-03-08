import { IEventLog } from 'app/entities/event-log/event-log.model';

export interface ITags {
  id?: number;
  name?: string | null;
  description?: string | null;
  eventLogs?: IEventLog[] | null;
}

export class Tags implements ITags {
  constructor(public id?: number, public name?: string | null, public description?: string | null, public eventLogs?: IEventLog[] | null) {}
}

export function getTagsIdentifier(tags: ITags): number | undefined {
  return tags.id;
}
